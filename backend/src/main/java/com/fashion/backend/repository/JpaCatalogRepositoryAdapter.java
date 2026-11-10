package com.fashion.backend.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fashion.backend.entity.Category;
import com.fashion.backend.entity.Product;
import com.fashion.backend.entity.ProductVariant;
import com.fashion.backend.repository.jpa.CategoryJpaRepository;
import com.fashion.backend.repository.jpa.ProductJpaRepository;
import com.fashion.backend.repository.jpa.ProductVariantJpaRepository;
import com.fashion.backend.repository.jpa.ReviewJpaRepository;
import com.fashion.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@Repository
@Transactional
public class JpaCatalogRepositoryAdapter implements CatalogRepository {
    private final ObjectMapper objectMapper;
    private final CategoryJpaRepository categoriesJpa;
    private final ProductJpaRepository productsJpa;
    private final ProductVariantJpaRepository variantsJpa;
    private final ReviewJpaRepository reviewsJpa;

    public JpaCatalogRepositoryAdapter(ObjectMapper objectMapper, CategoryJpaRepository categoriesJpa,
                                 ProductJpaRepository productsJpa, ProductVariantJpaRepository variantsJpa,
                                 ReviewJpaRepository reviewsJpa) {
        this.objectMapper = objectMapper;
        this.categoriesJpa = categoriesJpa;
        this.productsJpa = productsJpa;
        this.variantsJpa = variantsJpa;
        this.reviewsJpa = reviewsJpa;
    }

    @Override public List<Map<String, Object>> categories() {
        return categoriesJpa.findAllByOrderByIdAsc().stream().map(this::categoryRow).toList();
    }
    @Override public Map<String, Object> category(int id) {
        return categoryRow(categoriesJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục")));
    }
    @Override public int createCategory(Map<String, Object> b) {
        Category category = new Category();
        category.name = String.valueOf(b.get("name"));
        category.slug = String.valueOf(b.get("slug"));
        category.description = (String) b.get("description");
        Object parentId = b.get("parentId");
        if (parentId != null) category.parent = categoriesJpa.findById(Integer.parseInt(parentId.toString()))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục cha"));
        return categoriesJpa.save(category).id;
    }
    @Override public void updateCategory(int id, Map<String, Object> b) {
        Category category = categoriesJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
        category.name = String.valueOf(b.get("name"));
        category.slug = String.valueOf(b.get("slug"));
        category.description = (String) b.get("description");
        Object parentId = b.get("parentId");
        category.parent = parentId == null ? null : categoriesJpa.findById(Integer.parseInt(parentId.toString()))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục cha"));
        categoriesJpa.save(category);
    }
    @Override public void deleteCategory(int id) { categoriesJpa.deleteById(id); }

    @Override public List<Map<String, Object>> products(String search, Integer categoryId, BigDecimal minPrice,
                                                         BigDecimal maxPrice, int limit, int offset) {
        int page = offset / Math.max(limit, 1);
        return productsJpa.search(search, categoryId, minPrice, maxPrice,
            org.springframework.data.domain.PageRequest.of(page, limit,
                org.springframework.data.domain.Sort.by("createdAt").descending()))
            .getContent().stream().map(this::productRow).toList();
    }
    @Override public Map<String, Object> product(int id) {
        return productRow(productsJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm")));
    }
    @Override public Map<String, Object> productBySlug(String slug) {
        return productRow(productsJpa.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm")));
    }
    @Override public List<Map<String, Object>> categoryChildren(int id) {
        if (!categoriesJpa.existsById(id)) throw new ResourceNotFoundException("Không tìm thấy danh mục");
        return categoriesJpa.findByParentIdOrderByIdAsc(id).stream().map(this::categoryRow).toList();
    }
    @Override public List<Map<String, Object>> variantsForProduct(int id) {
        return variantsJpa.findByProductIdOrderByPriceAscIdAsc(id).stream().map(this::variantRow).toList();
    }
    @Override public List<Map<String, Object>> reviewsForProduct(int id) {
        return reviewsJpa.findByProductIdOrderByCreatedAtDesc(id).stream().map(review -> {
            Map<String,Object> row=new java.util.LinkedHashMap<>();
            row.put("id",review.id); row.put("user_id",review.user.id); row.put("product_id",review.product.id);
            row.put("rating",review.rating); row.put("comment",review.comment); row.put("created_at",review.createdAt);
            row.put("fullname",review.user.fullname);
            return row;
        }).toList();
    }
    @Override public List<Map<String, Object>> adminProducts() {
        return productsJpa.findAll(org.springframework.data.domain.Sort.by("id").descending()).stream().map(product -> {
            Map<String,Object> row=productRow(product);
            variantsJpa.findByProductIdOrderByIdAsc(product.id).stream().findFirst().ifPresent(variant -> {
                row.put("sku",variant.sku); row.put("size",variant.size); row.put("color",variant.color);
                row.put("price",variant.price); row.put("sale_price",variant.salePrice); row.put("stock",variant.stock);
            });
            return row;
        }).toList();
    }
    @Override public int createProduct(Map<String, Object> b) {
        Product product = new Product();
        product.category = categoriesJpa.findById(Integer.parseInt(b.get("categoryId").toString()))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
        product.name = String.valueOf(b.get("name"));
        product.slug = String.valueOf(b.get("slug"));
        product.description = (String) b.get("description");
        product.thumbnail = (String) b.get("thumbnail");
        product.images = jsonValue(b.get("images")) == null ? null : jsonValue(b.get("images")).toString();
        product.active = b.get("isActive") == null || "1".equals(b.get("isActive").toString())
            || "true".equalsIgnoreCase(b.get("isActive").toString());
        return productsJpa.save(product).id;
    }
    @Override public void updateProduct(int id, Map<String, Object> b) {
        Product product = productsJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        product.category = categoriesJpa.findById(Integer.parseInt(b.get("categoryId").toString()))
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục"));
        product.name = String.valueOf(b.get("name"));
        product.slug = String.valueOf(b.get("slug"));
        product.description = (String) b.get("description");
        product.thumbnail = (String) b.get("thumbnail");
        if (b.get("images") != null) {
            Object images = jsonValue(b.get("images"));
            product.images = images == null ? null : images.toString();
        }
        if (b.get("isActive") != null) product.active = "1".equals(b.get("isActive").toString())
            || "true".equalsIgnoreCase(b.get("isActive").toString());
        productsJpa.save(product);
    }
    @Override public void deleteProduct(int id) { productsJpa.deleteById(id); }
    @Override public List<Map<String, Object>> variants() {
        return variantsJpa.findAllByOrderByIdDesc().stream().map(this::variantRow).toList();
    }
    @Override public int createVariant(int productId, Map<String, Object> b) {
        ProductVariant variant = new ProductVariant();
        variant.product = productsJpa.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        setVariant(variant, b);
        return variantsJpa.save(variant).id;
    }
    @Override public void updateVariant(int id, int productId, Map<String, Object> b) {
        ProductVariant variant = variantsJpa.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy biến thể"));
        variant.product = productsJpa.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm"));
        setVariant(variant, b);
        variantsJpa.save(variant);
    }
    @Override public void deleteVariant(int id) { variantsJpa.deleteById(id); }
    @Override public Integer firstVariantId(int productId) {
        return variantsJpa.findByProductIdOrderByIdAsc(productId).stream()
            .findFirst().map(variant -> variant.id).orElse(null);
    }

    private Map<String, Object> categoryRow(Category category) {
        Map<String, Object> row = new java.util.LinkedHashMap<>();
        row.put("id", category.id);
        row.put("name", category.name);
        row.put("slug", category.slug);
        row.put("parent_id", category.parent == null ? null : category.parent.id);
        row.put("description", category.description);
        row.put("created_at", category.createdAt);
        return row;
    }

    private Map<String, Object> productRow(Product product) {
        Map<String, Object> row = new java.util.LinkedHashMap<>();
        row.put("id", product.id);
        row.put("category_id", product.category.id);
        row.put("category_name", product.category.name);
        row.put("name", product.name);
        row.put("slug", product.slug);
        row.put("description", product.description);
        row.put("thumbnail", product.thumbnail);
        row.put("images", product.images);
        row.put("is_active", product.active);
        row.put("created_at", product.createdAt);
        row.put("updated_at", product.updatedAt);
        List<ProductVariant> variants = variantsJpa.findByProductIdOrderByPriceAscIdAsc(product.id);
        row.put("sale_price", variants.stream().map(v -> v.salePrice).filter(java.util.Objects::nonNull)
            .min(java.math.BigDecimal::compareTo).orElse(null));
        row.put("min_price", variants.stream().map(v -> v.price).min(java.math.BigDecimal::compareTo).orElse(null));
        return row;
    }

    private Map<String,Object> variantRow(ProductVariant variant) {
        Map<String,Object> row=new java.util.LinkedHashMap<>();
        row.put("id",variant.id); row.put("product_id",variant.product.id); row.put("sku",variant.sku);
        row.put("size",variant.size); row.put("color",variant.color); row.put("price",variant.price);
        row.put("sale_price",variant.salePrice); row.put("stock",variant.stock);
        row.put("created_at",variant.createdAt); row.put("product_name",variant.product.name);
        return row;
    }

    private void setVariant(ProductVariant variant, Map<String, Object> body) {
        variant.sku = String.valueOf(body.get("sku"));
        variant.size = String.valueOf(body.get("size"));
        variant.color = String.valueOf(body.get("color"));
        variant.price = new java.math.BigDecimal(body.get("price").toString());
        Object sale = salePrice(body);
        variant.salePrice = sale == null || sale.toString().isBlank() ? null : new java.math.BigDecimal(sale.toString());
        variant.stock = Integer.parseInt(body.getOrDefault("stock", 0).toString());
    }

    private Object salePrice(Map<String, Object> body) {
        Object value = body.get("salePrice");
        return value != null ? value : body.get("sale_price");
    }

    private Object jsonValue(Object value) {
        if (value == null || value instanceof String) return value;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new IllegalArgumentException("images phải là JSON hợp lệ", ex);
        }
    }
}
