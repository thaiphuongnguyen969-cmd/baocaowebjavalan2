package com.fashion.backend.service;

import java.util.List;
import java.util.Map;

public interface CommerceService {
    List<Map<String,Object>> coupons();
    List<Map<String,Object>> adminCoupons();
    int createCoupon(Map<String,Object> body);
    void updateCoupon(int id,Map<String,Object> body);
    void deleteCoupon(int id);
    Map<String,Object> addCart(Map<String,Object> body);
    List<Map<String,Object>> cart(String sessionId);
    void updateCartItem(String sessionId, int id, int quantity);
    void removeCart(String sessionId, int id);
    List<Map<String,Object>> orders();
    List<Map<String,Object>> ordersForUser(String email);
    Map<String,Object> order(int id);
    void updateOrderStatus(int id,Map<String,String> body);
    void deleteOrder(int id);
    Map<String,Object> checkout(Map<String,Object> body);
    Map<String,Object> checkout(Map<String,Object> body, String userEmail);
    Map<String,Object> track(String code);
}
