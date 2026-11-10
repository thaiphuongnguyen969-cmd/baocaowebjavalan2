package com.fashion.backend.repository;

import java.util.List;
import java.util.Map;

public interface CommerceRepository {
    List<Map<String,Object>> activeCoupons();
    List<Map<String,Object>> adminCoupons();
    int createCoupon(Map<String,Object> body);
    void updateCoupon(int id,Map<String,Object> body);
    void deleteCoupon(int id);
    Integer cartId(String sessionId);
    int createCart(String sessionId);
    boolean cartItemExists(int cartId,int variantId);
    void incrementCartItem(int cartId,int variantId,int quantity);
    void updateCartItemQuantity(int itemId,int quantity,String sessionId);
    void createCartItem(int cartId,int variantId,int quantity);
    List<Map<String,Object>> cart(String sessionId);
    void deleteCartItem(int id, String sessionId);
    List<Map<String,Object>> orders();
    Map<String,Object> order(int id);
    List<Map<String,Object>> orderItems(int id);
    void updateOrderStatus(int id,String orderStatus,String paymentStatus);
    void deleteOrder(int id);
    Map<String,Object> variantForCheckout(int id);
    Map<String,Object> validCoupon(String code);
    void incrementCouponUse(int id);
    int createOrder(Map<String,Object> order, String code, java.math.BigDecimal total,
                    java.math.BigDecimal discount, java.math.BigDecimal shipping,
                    java.math.BigDecimal finalAmount);
    void createOrderItem(int orderId,Map<String,Object> variant,java.math.BigDecimal unitPrice,int quantity);
    boolean decreaseStock(int variantId,int quantity);
    Map<String,Object> orderByCode(String code);
    List<Map<String,Object>> ordersForUser(int userId);
}
