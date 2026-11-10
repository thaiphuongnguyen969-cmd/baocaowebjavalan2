package com.fashion.backend.service;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public interface CatalogService {
    List<Map<String, Object>> categories();
    Map<String, Object> category(int id);
    List<Map<String, Object>> categoryChildren(int id);
    int createCategory(Map<String, Object> body);
    void updateCategory(int id, Map<String, Object> body);
    void deleteCategory(int id);
    List<Map<String, Object>> products(String search, Integer categoryId, BigDecimal minPrice,
                                       BigDecimal maxPrice, int page, int size);
    Map<String, Object> product(int id);
    Map<String, Object> productBySlug(String slug);
    List<Map<String, Object>> adminProducts();
    int createProduct(Map<String, Object> body);
    void updateProduct(int id, Map<String, Object> body);
    void deleteProduct(int id);
    List<Map<String, Object>> variants();
    int createVariant(Map<String, Object> body);
    void updateVariant(int id, Map<String, Object> body);
    void deleteVariant(int id);
}
