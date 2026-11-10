package com.fashion.backend.repository;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public interface CatalogRepository {
    List<Map<String, Object>> categories();
    Map<String, Object> category(int id);
    int createCategory(Map<String, Object> body);
    void updateCategory(int id, Map<String, Object> body);
    void deleteCategory(int id);
    List<Map<String, Object>> products(String search, Integer categoryId, BigDecimal minPrice,
                                       BigDecimal maxPrice, int limit, int offset);
    Map<String, Object> product(int id);
    Map<String, Object> productBySlug(String slug);
    List<Map<String, Object>> categoryChildren(int id);
    List<Map<String, Object>> variantsForProduct(int productId);
    List<Map<String, Object>> reviewsForProduct(int productId);
    List<Map<String, Object>> adminProducts();
    int createProduct(Map<String, Object> body);
    void updateProduct(int id, Map<String, Object> body);
    void deleteProduct(int id);
    List<Map<String, Object>> variants();
    int createVariant(int productId, Map<String, Object> body);
    void updateVariant(int id, int productId, Map<String, Object> body);
    void deleteVariant(int id);
    Integer firstVariantId(int productId);
}
