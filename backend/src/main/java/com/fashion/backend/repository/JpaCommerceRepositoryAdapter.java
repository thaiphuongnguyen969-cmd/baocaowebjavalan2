package com.fashion.backend.repository;

import com.fashion.backend.entity.*;
import com.fashion.backend.exception.ResourceNotFoundException;
import com.fashion.backend.repository.jpa.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class JpaCommerceRepositoryAdapter implements CommerceRepository {
    private final CouponJpaRepository couponsJpa;
    private final CartJpaRepository cartsJpa;
    private final CartItemJpaRepository cartItemsJpa;
    private final UserJpaRepository usersJpa;
    private final ProductVariantJpaRepository variantsJpa;
    private final ShopOrderJpaRepository ordersJpa;
    private final OrderItemJpaRepository orderItemsJpa;
    public JpaCommerceRepositoryAdapter(CouponJpaRepository couponsJpa, CartJpaRepository cartsJpa,
                                  CartItemJpaRepository cartItemsJpa, UserJpaRepository usersJpa,
                                  ProductVariantJpaRepository variantsJpa, ShopOrderJpaRepository ordersJpa,
                                  OrderItemJpaRepository orderItemsJpa) {
        this.couponsJpa=couponsJpa;
        this.cartsJpa=cartsJpa;
        this.cartItemsJpa=cartItemsJpa;
        this.usersJpa=usersJpa;
        this.variantsJpa=variantsJpa;
        this.ordersJpa=ordersJpa;
        this.orderItemsJpa=orderItemsJpa;
    }

    @Override public List<Map<String,Object>> activeCoupons() {
        return couponsJpa.findActive(java.time.LocalDateTime.now()).stream().map(this::couponRow).toList();
    }
    @Override public List<Map<String,Object>> adminCoupons() {
        return couponsJpa.findAllByOrderByIdDesc().stream().map(this::couponRow).toList();
    }
    @Override public int createCoupon(Map<String,Object> b) {
        Coupon coupon = new Coupon();
        setCoupon(coupon,b);
        coupon.usedCount = 0;
        return couponsJpa.save(coupon).id;
    }
    @Override public void updateCoupon(int id,Map<String,Object> b) {
        Coupon coupon = couponsJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá"));
        setCoupon(coupon,b);
        couponsJpa.save(coupon);
    }
    @Override public void deleteCoupon(int id) { couponsJpa.deleteById(id); }
    @Override public Integer cartId(String sessionId) {
        return cartsJpa.findFirstBySessionId(sessionId).map(cart -> cart.id).orElse(null);
    }
    @Override public int createCart(String sessionId) {
        Cart cart = new Cart();
        cart.sessionId = sessionId;
        return cartsJpa.save(cart).id;
    }
    @Override public boolean cartItemExists(int cartId,int variantId) {
        return cartItemsJpa.findByCartIdAndVariantId(cartId,variantId).isPresent();
    }
    @Override public void incrementCartItem(int cartId,int variantId,int quantity) {
        CartItem item = cartItemsJpa.findByCartIdAndVariantId(cartId,variantId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ"));
        item.quantity += quantity;
        cartItemsJpa.save(item);
    }
    @Override public void createCartItem(int cartId,int variantId,int quantity) {
        CartItem item = new CartItem();
        item.cart = cartsJpa.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng"));
        item.variant = variantsJpa.findById(variantId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể"));
        item.quantity = quantity;
        cartItemsJpa.save(item);
    }
    @Override public List<Map<String,Object>> cart(String sessionId) {
        return cartItemsJpa.findByCartSessionId(sessionId).stream().map(this::cartRow).toList();
    }
    @Override public void updateCartItemQuantity(int itemId,int quantity,String sessionId) {
        CartItem item=cartItemsJpa.findByIdAndCartSessionId(itemId,sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ"));
        if(item.variant.stock<quantity) throw new com.fashion.backend.exception.BadRequestException("Số lượng vượt tồn kho");
        item.quantity=quantity;
        cartItemsJpa.save(item);
    }
    @Override public void deleteCartItem(int id, String sessionId) {
        CartItem item = cartItemsJpa.findByIdAndCartSessionId(id, sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm trong giỏ"));
        cartItemsJpa.delete(item);
    }
    @Override public List<Map<String,Object>> orders() {
        return ordersJpa.findAllByOrderByCreatedAtDesc().stream().map(this::orderRow).toList();
    }
    @Override public Map<String,Object> order(int id) { return orderRow(ordersJpa.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"))); }
    @Override public List<Map<String,Object>> orderItems(int id) {
        return orderItemsJpa.findByOrderId(id).stream().map(this::orderItemRow).toList();
    }
    @Override public void updateOrderStatus(int id,String orderStatus,String paymentStatus) {
        ShopOrder order=ordersJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        order.orderStatus=orderStatus; order.paymentStatus=paymentStatus; ordersJpa.save(order);
    }
    @Override public void deleteOrder(int id) { ordersJpa.deleteById(id); }
    @Override public Map<String,Object> variantForCheckout(int id) {
        ProductVariant variant=variantsJpa.findForCheckout(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể sản phẩm"));
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",variant.id); row.put("product_id",variant.product.id); row.put("product_name",variant.product.name);
        row.put("sku",variant.sku); row.put("size",variant.size); row.put("color",variant.color);
        row.put("price",variant.price); row.put("sale_price",variant.salePrice); row.put("stock",variant.stock);
        return row;
    }
    @Override public Map<String,Object> validCoupon(String code) {
        Coupon coupon=couponsJpa.findByCodeIgnoreCase(code).orElse(null);
        if(coupon==null||!coupon.expiresAt.isAfter(java.time.LocalDateTime.now())
                ||coupon.usageLimit!=null&&coupon.usedCount>=coupon.usageLimit) return null;
        return couponRow(coupon);
    }
    @Override public void incrementCouponUse(int id) {
        Coupon coupon=couponsJpa.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá"));
        coupon.usedCount++;
        couponsJpa.save(coupon);
    }
    @Override public int createOrder(Map<String,Object> b,String code,BigDecimal total,BigDecimal discount,
                                     BigDecimal shipping,BigDecimal finalAmount) {
        ShopOrder order=new ShopOrder();
        Object userId=b.get("userId");
        if(userId!=null) order.user=usersJpa.findById(Integer.parseInt(userId.toString()))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        order.orderCode=code; order.receiverName=String.valueOf(b.get("receiverName"));
        order.receiverPhone=String.valueOf(b.get("receiverPhone")); order.receiverAddress=String.valueOf(b.get("receiverAddress"));
        order.totalAmount=total; order.discountAmount=discount; order.shippingFee=shipping; order.finalAmount=finalAmount;
        order.paymentMethod=String.valueOf(b.getOrDefault("paymentMethod","COD"));
        order.paymentStatus="pending"; order.orderStatus="pending"; order.note=(String)b.get("note");
        return ordersJpa.save(order).id;
    }
    @Override public void createOrderItem(int orderId,Map<String,Object> v,BigDecimal unitPrice,int quantity) {
        OrderItem item=new OrderItem();
        item.order=ordersJpa.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        item.variant=variantsJpa.findById(((Number)v.get("id")).intValue())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể sản phẩm"));
        item.productName=String.valueOf(v.get("product_name")); item.size=String.valueOf(v.get("size"));
        item.color=String.valueOf(v.get("color")); item.price=unitPrice; item.quantity=quantity;
        orderItemsJpa.save(item);
    }
    @Override public boolean decreaseStock(int variantId,int quantity) {
        return variantsJpa.decrementStock(variantId,quantity)==1;
    }
    @Override public Map<String,Object> orderByCode(String code) {
        return orderRow(ordersJpa.findByOrderCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng")));
    }

    @Override public List<Map<String,Object>> ordersForUser(int userId) {
        return ordersJpa.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::orderRow).toList();
    }
    private Map<String,Object> couponRow(Coupon coupon) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",coupon.id); row.put("code",coupon.code); row.put("discount_type",coupon.discountType);
        row.put("discount_value",coupon.discountValue); row.put("min_order_value",coupon.minOrderValue);
        row.put("usage_limit",coupon.usageLimit); row.put("used_count",coupon.usedCount);
        row.put("expires_at",coupon.expiresAt); row.put("created_at",coupon.createdAt);
        return row;
    }

    private void setCoupon(Coupon coupon,Map<String,Object> body) {
        coupon.code=String.valueOf(body.get("code"));
        coupon.discountType=String.valueOf(body.get("discountType"));
        coupon.discountValue=new BigDecimal(body.get("discountValue").toString());
        Object min=body.get("minOrderValue");
        coupon.minOrderValue=min==null?BigDecimal.ZERO:new BigDecimal(min.toString());
        Object limit=body.get("usageLimit");
        coupon.usageLimit=limit==null||limit.toString().isBlank()?null:Integer.valueOf(limit.toString());
        Object expires=body.get("expiresAt");
        coupon.expiresAt=expires instanceof java.time.LocalDateTime date?date:
            java.time.LocalDateTime.parse(expires.toString().replace(' ','T'));
    }

    private Map<String,Object> cartRow(CartItem item) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        ProductVariant variant=item.variant;
        row.put("id",item.id); row.put("quantity",item.quantity); row.put("variant_id",variant.id);
        row.put("size",variant.size); row.put("color",variant.color); row.put("price",variant.price);
        row.put("sale_price",variant.salePrice); row.put("stock",variant.stock);
        row.put("product_name",variant.product.name); row.put("thumbnail",variant.product.thumbnail);
        row.put("product_id",variant.product.id);
        return row;
    }

    private Map<String,Object> orderRow(ShopOrder order) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",order.id); row.put("user_id",order.user==null?null:order.user.id);
        row.put("order_code",order.orderCode); row.put("receiver_name",order.receiverName);
        row.put("receiver_phone",order.receiverPhone); row.put("receiver_address",order.receiverAddress);
        row.put("total_amount",order.totalAmount); row.put("discount_amount",order.discountAmount);
        row.put("shipping_fee",order.shippingFee); row.put("final_amount",order.finalAmount);
        row.put("payment_method",order.paymentMethod); row.put("payment_status",order.paymentStatus);
        row.put("order_status",order.orderStatus); row.put("note",order.note);
        row.put("created_at",order.createdAt); row.put("updated_at",order.updatedAt);
        return row;
    }

    private Map<String,Object> orderItemRow(OrderItem item) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",item.id); row.put("order_id",item.order.id);
        row.put("product_variant_id",item.variant.id); row.put("product_name",item.productName);
        row.put("size",item.size); row.put("color",item.color); row.put("price",item.price);
        row.put("quantity",item.quantity);
        return row;
    }
}
