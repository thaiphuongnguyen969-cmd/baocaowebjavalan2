package com.fashion.backend.service.impl;

import com.fashion.backend.exception.BadRequestException;
import com.fashion.backend.exception.ResourceNotFoundException;
import com.fashion.backend.repository.CatalogRepository;
import com.fashion.backend.service.CatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CatalogServiceImpl implements CatalogService {
    private final CatalogRepository repository;

    public CatalogServiceImpl(CatalogRepository repository) { this.repository = repository; }

    @Override public List<Map<String, Object>> categories() { return repository.categories(); }
    @Override public Map<String, Object> category(int id) { return repository.category(id); }
    @Override public List<Map<String, Object>> categoryChildren(int id) { return repository.categoryChildren(id); }
    @Override public int createCategory(Map<String, Object> body) { return repository.createCategory(body); }
    @Override public void updateCategory(int id, Map<String, Object> body) { repository.updateCategory(id, body); }
    @Override public void deleteCategory(int id) { repository.deleteCategory(id); }

    @Override public List<Map<String, Object>> products(String search, Integer categoryId, BigDecimal minPrice,
                                                        BigDecimal maxPrice, int page, int size) {
        if (page < 1 || size < 1 || size > 100) throw new BadRequestException("page phải >= 1, size phải từ 1 đến 100");
        if (minPrice != null && minPrice.signum() < 0 || maxPrice != null && maxPrice.signum() < 0
                || minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new BadRequestException("Khoảng giá không hợp lệ");
        }
        return repository.products(search, categoryId, minPrice, maxPrice, size, (page - 1) * size);
    }

    @Override public Map<String, Object> product(int id) {
        Map<String, Object> product;
        try {
            product = repository.product(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            throw new ResourceNotFoundException("Không tìm thấy sản phẩm");
        }
        product.put("variants", repository.variantsForProduct(id));
        product.put("reviews", repository.reviewsForProduct(id));
        return product;
    }

    @Override public Map<String, Object> productBySlug(String slug) {
        Map<String, Object> product = repository.productBySlug(slug);
        int id = ((Number) product.get("id")).intValue();
        product.put("variants", repository.variantsForProduct(id));
        product.put("reviews", repository.reviewsForProduct(id));
        return product;
    }

    @Override public List<Map<String, Object>> adminProducts() { return repository.adminProducts(); }

    @Override
    @Transactional
    public int createProduct(Map<String, Object> body) {
        require(body, "categoryId", "name", "slug");
        int id = repository.createProduct(body);
        if (body.get("price") != null) {
            body.put("price", money(body.get("price"), "price"));
            body.put("salePrice", optionalMoney(body.get("salePrice") != null
                ? body.get("salePrice") : body.get("sale_price")));
            body.put("stock", nonNegativeInt(body.getOrDefault("stock", 0), "stock"));
            body.put("sku", textOr(body.get("sku"), "SP-" + id));
            body.put("size", textOr(body.get("size"), "Free"));
            body.put("color", textOr(body.get("color"), "Đen"));
            repository.createVariant(id, body);
        }
        return id;
    }

    @Override
    @Transactional
    public void updateProduct(int id, Map<String, Object> body) {
        require(body, "categoryId", "name", "slug");
        repository.updateProduct(id, body);
        if (body.get("price") != null) {
            body.put("price", money(body.get("price"), "price"));
            body.put("salePrice", optionalMoney(body.get("salePrice") != null
                ? body.get("salePrice") : body.get("sale_price")));
            body.put("stock", nonNegativeInt(body.getOrDefault("stock", 0), "stock"));
            body.put("sku", textOr(body.get("sku"), "SP-" + id));
            body.put("size", textOr(body.get("size"), "Free"));
            body.put("color", textOr(body.get("color"), "Đen"));
            Integer variantId = repository.firstVariantId(id);
            if (variantId == null) repository.createVariant(id, body);
            else repository.updateVariant(variantId, id, body);
        }
    }

    @Override public void deleteProduct(int id) { repository.deleteProduct(id); }
    @Override public List<Map<String, Object>> variants() { return repository.variants(); }

    @Override public int createVariant(Map<String, Object> body) {
        int productId = requiredInt(body, "productId", "product_id");
        require(body, "sku", "size", "color", "price");
        body.put("price", money(body.get("price"), "price"));
        body.put("salePrice", optionalMoney(body.get("salePrice") != null ? body.get("salePrice") : body.get("sale_price")));
        body.put("stock", nonNegativeInt(body.getOrDefault("stock", 0), "stock"));
        return repository.createVariant(productId, body);
    }

    @Override public void updateVariant(int id, Map<String, Object> body) {
        int productId = requiredInt(body, "productId", "product_id");
        require(body, "sku", "size", "color", "price");
        body.put("price", money(body.get("price"), "price"));
        body.put("salePrice", optionalMoney(body.get("salePrice") != null ? body.get("salePrice") : body.get("sale_price")));
        body.put("stock", nonNegativeInt(body.getOrDefault("stock", 0), "stock"));
        repository.updateVariant(id, productId, body);
    }
    @Override public void deleteVariant(int id) { repository.deleteVariant(id); }

    private void require(Map<String, Object> body, String... fields) {
        for (String field : fields) {
            Object value = body.get(field);
            if (value == null || value.toString().isBlank()) throw new BadRequestException("Thiếu trường " + field);
        }
    }

    private BigDecimal money(Object value, String field) {
        if (value == null) throw new BadRequestException("Thiếu trường " + field);
        try {
            BigDecimal amount = new BigDecimal(value.toString().trim());
            if (amount.signum() < 0) throw new BadRequestException(field + " không được âm");
            return amount;
        } catch (NumberFormatException ex) {
            throw new BadRequestException(field + " phải là số");
        }
    }
    private BigDecimal optionalMoney(Object value) {
        return value == null || value.toString().isBlank() ? null : money(value, "salePrice");
    }
    private int nonNegativeInt(Object value, String field) {
        try {
            int result = Integer.parseInt(value.toString().trim());
            if (result < 0) throw new BadRequestException(field + " không được âm");
            return result;
        } catch (NumberFormatException ex) {
            throw new BadRequestException(field + " phải là số nguyên");
        }
    }
    private int requiredInt(Map<String, Object> body, String... names) {
        for (String name : names) if (body.get(name) != null) return nonNegativeInt(body.get(name), name);
        throw new BadRequestException("Thiếu trường " + names[0]);
    }
    private String textOr(Object value, String defaultValue) {
        return value == null || value.toString().isBlank() ? defaultValue : value.toString().trim();
    }
}
