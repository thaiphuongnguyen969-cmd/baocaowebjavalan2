package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "order_id", nullable = false) public ShopOrder order;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "product_variant_id", nullable = false) public ProductVariant variant;
    @Column(name = "product_name", nullable = false, length = 150) public String productName;
    @Column(nullable = false, length = 20) public String size;
    @Column(nullable = false, length = 50) public String color;
    @Column(nullable = false, precision = 10, scale = 2) public BigDecimal price;
    @Column(nullable = false) public Integer quantity;
    public OrderItem() {}
}
