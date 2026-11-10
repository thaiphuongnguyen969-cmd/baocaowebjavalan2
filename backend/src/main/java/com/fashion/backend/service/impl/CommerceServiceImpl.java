package com.fashion.backend.service.impl;

import com.fashion.backend.exception.BadRequestException;
import com.fashion.backend.exception.ResourceNotFoundException;
import com.fashion.backend.repository.CommerceRepository;
import com.fashion.backend.repository.UserRepository;
import com.fashion.backend.service.CommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CommerceServiceImpl implements CommerceService {
    private final CommerceRepository repository;
    private final UserRepository userRepository;
    public CommerceServiceImpl(CommerceRepository repository) { this(repository,null); }
    @Autowired
    public CommerceServiceImpl(CommerceRepository repository,UserRepository userRepository) {
        this.repository=repository;
        this.userRepository=userRepository;
    }

    @Override public List<Map<String,Object>> coupons() { return repository.activeCoupons(); }
    @Override public List<Map<String,Object>> adminCoupons() { return repository.adminCoupons(); }
    @Override public int createCoupon(Map<String,Object> b) { return repository.createCoupon(b); }
    @Override public void updateCoupon(int id,Map<String,Object> b) { repository.updateCoupon(id,b); }
    @Override public void deleteCoupon(int id) { repository.deleteCoupon(id); }

    @Override @Transactional
    public Map<String,Object> addCart(Map<String,Object> b) {
        String sessionId=String.valueOf(b.getOrDefault("sessionId","guest-"+UUID.randomUUID()));
        int variantId=positiveInt(b.get("variantId"),"variantId");
        int quantity=positiveInt(b.get("quantity"),"quantity");
        Map<String,Object> variant=repository.variantForCheckout(variantId);
        int existingQuantity=repository.cart(sessionId).stream()
            .filter(item -> ((Number)item.get("variant_id")).intValue()==variantId)
            .mapToInt(item -> ((Number)item.get("quantity")).intValue()).sum();
        if(existingQuantity+quantity>((Number)variant.get("stock")).intValue()) {
            throw new BadRequestException("Số lượng vượt tồn kho");
        }
        Integer cartId=repository.cartId(sessionId);
        if(cartId==null) cartId=repository.createCart(sessionId);
        if(repository.cartItemExists(cartId,variantId)) repository.incrementCartItem(cartId,variantId,quantity);
        else repository.createCartItem(cartId,variantId,quantity);
        return Map.of("sessionId",sessionId,"message","Đã thêm vào giỏ hàng");
    }
    @Override public List<Map<String,Object>> cart(String sessionId) { return repository.cart(sessionId); }
    @Override public void updateCartItem(String sessionId,int id,int quantity) {
        if(sessionId==null||sessionId.isBlank()) throw new BadRequestException("Thiếu mã phiên giỏ hàng");
        repository.updateCartItemQuantity(id,positiveInt(quantity,"quantity"),sessionId);
    }
    @Override public void removeCart(String sessionId, int id) {
        if (sessionId == null || sessionId.isBlank()) throw new BadRequestException("Thiếu mã phiên giỏ hàng");
        repository.deleteCartItem(id, sessionId);
    }
    @Override public List<Map<String,Object>> orders() { return repository.orders(); }
    @Override public List<Map<String,Object>> ordersForUser(String email) {
        if(userRepository==null) throw new IllegalStateException("User repository is required for account order history");
        var user=userRepository.findByEmail(email);
        if(user==null) throw new ResourceNotFoundException("Không tìm thấy người dùng");
        return repository.ordersForUser(user.id());
    }
    @Override public Map<String,Object> order(int id) {
        Map<String,Object> result;
        try { result=repository.order(id); }
        catch(org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Không tìm thấy đơn hàng");
        }
        result.put("items",repository.orderItems(id));
        return result;
    }
    @Override public void updateOrderStatus(int id,Map<String,String> body) {
        Map<String,Object> existing=order(id);
        repository.updateOrderStatus(id,
            body.getOrDefault("orderStatus",String.valueOf(existing.get("order_status"))),
            body.getOrDefault("paymentStatus",String.valueOf(existing.get("payment_status"))));
    }
    @Override public void deleteOrder(int id) { repository.deleteOrder(id); }

    @Override @Transactional
    public Map<String,Object> checkout(Map<String,Object> b) {
        return checkout(b, null);
    }

    @Override @Transactional
    public Map<String,Object> checkout(Map<String,Object> b, String userEmail) {
        b = new HashMap<>(b);
        if (userEmail == null) {
            b.put("userId", null);
        } else {
            if (userRepository == null) throw new IllegalStateException("User repository is required for authenticated checkout");
            var user = userRepository.findByEmail(userEmail);
            if (user == null) throw new ResourceNotFoundException("Không tìm thấy người dùng");
            b.put("userId", user.id());
        }
        requireText(b,"receiverName");
        requireText(b,"receiverPhone");
        requireText(b,"receiverAddress");
        Object rawItems=b.get("items");
        if(!(rawItems instanceof List<?> list)||list.isEmpty()) throw new BadRequestException("Giỏ hàng trống");
        List<CheckoutItem> items=new ArrayList<>();
        Map<Integer,Integer> quantities=new LinkedHashMap<>();
        for(Object raw:list) {
            if(!(raw instanceof Map<?,?> item)) throw new BadRequestException("Dữ liệu sản phẩm trong giỏ không hợp lệ");
            int variantId=positiveInt(item.get("variantId"),"variantId");
            int quantity=positiveInt(item.get("quantity"),"quantity");
            quantities.merge(variantId,quantity,Integer::sum);
        }
        BigDecimal total=BigDecimal.ZERO;
        for(var entry:quantities.entrySet()) {
            Map<String,Object> variant;
            try { variant=repository.variantForCheckout(entry.getKey()); }
            catch(org.springframework.dao.EmptyResultDataAccessException ex) {
                throw new ResourceNotFoundException("Không tìm thấy biến thể sản phẩm");
            }
            int quantity=entry.getValue();
            int stock=((Number)variant.get("stock")).intValue();
            if(quantity>stock) throw new BadRequestException("Số lượng vượt tồn kho");
            BigDecimal price=toDecimal(variant.get("sale_price")!=null?variant.get("sale_price"):variant.get("price"));
            total=total.add(price.multiply(BigDecimal.valueOf(quantity)));
            items.add(new CheckoutItem(variant,quantity,price));
        }
        BigDecimal discount=BigDecimal.ZERO;
        String couponCode=String.valueOf(b.getOrDefault("couponCode",""));
        if(!couponCode.isBlank()) {
            Map<String,Object> coupon=repository.validCoupon(couponCode);
            if(coupon!=null && total.compareTo(toDecimal(coupon.get("min_order_value")))>=0) {
                BigDecimal value=toDecimal(coupon.get("discount_value"));
                discount="percent".equals(coupon.get("discount_type"))
                    ?total.multiply(value).divide(BigDecimal.valueOf(100)):value;
                discount=discount.min(total);
                repository.incrementCouponUse(((Number)coupon.get("id")).intValue());
            }
        }
        BigDecimal shipping=total.compareTo(BigDecimal.valueOf(500000))>=0
            ?BigDecimal.ZERO:BigDecimal.valueOf(30000);
        BigDecimal finalAmount=total.subtract(discount).add(shipping);
        String code="FS"+System.currentTimeMillis()+UUID.randomUUID().toString().substring(0,6).toUpperCase(Locale.ROOT);
        int orderId=repository.createOrder(b,code,total,discount,shipping,finalAmount);
        for(CheckoutItem item:items) {
            repository.createOrderItem(orderId,item.variant(),item.price(),item.quantity());
            if(!repository.decreaseStock(((Number)item.variant().get("id")).intValue(),item.quantity())) {
                throw new BadRequestException("Số lượng vượt tồn kho");
            }
        }
        return Map.of("orderCode",code,"total",total,"discount",discount,
            "shippingFee",shipping,"finalAmount",finalAmount);
    }

    @Override public Map<String,Object> track(String code) {
        Map<String,Object> result;
        try { result=repository.orderByCode(code); }
        catch(org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Không tìm thấy đơn hàng");
        }
        result.put("items",repository.orderItems(((Number)result.get("id")).intValue()));
        return result;
    }

    private int positiveInt(Object value,String field) {
        if(value==null) throw new BadRequestException("Thiếu trường "+field);
        try {
            int result=Integer.parseInt(value.toString());
            if(result<1) throw new BadRequestException(field+" phải lớn hơn 0");
            return result;
        } catch(NumberFormatException ex) { throw new BadRequestException(field+" phải là số nguyên"); }
    }
    private BigDecimal toDecimal(Object value) {
        if(value instanceof BigDecimal decimal) return decimal;
        return new BigDecimal(String.valueOf(value));
    }
    private void requireText(Map<String,Object> body,String key) {
        Object value=body.get(key);
        if(value==null||value.toString().isBlank()) throw new BadRequestException("Thiếu trường "+key);
    }
    private record CheckoutItem(Map<String,Object> variant,int quantity,BigDecimal price) {}
}
