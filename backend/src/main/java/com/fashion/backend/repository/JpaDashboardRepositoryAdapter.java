package com.fashion.backend.repository;

import com.fashion.backend.repository.jpa.*;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public class JpaDashboardRepositoryAdapter implements DashboardRepository {
    private final UserJpaRepository users;
    private final ProductJpaRepository products;
    private final ShopOrderJpaRepository orders;
    private final ProductVariantJpaRepository variants;
    public JpaDashboardRepositoryAdapter(UserJpaRepository users,ProductJpaRepository products,
                                   ShopOrderJpaRepository orders,ProductVariantJpaRepository variants) {
        this.users=users; this.products=products; this.orders=orders; this.variants=variants;
    }
    @Override public Map<String,Object> summary() {
        List<Map<String,Object>> lowStock=variants.findByStockLessThanOrderByStockAsc(10).stream().map(variant ->
            Map.<String,Object>of("name",variant.product.name,"sku",variant.sku,"stock",variant.stock)).toList();
        return Map.of("users",users.count(),"products",products.count(),"orders",orders.count(),
            "revenue",orders.paidRevenue(),"lowStock",lowStock);
    }
}
