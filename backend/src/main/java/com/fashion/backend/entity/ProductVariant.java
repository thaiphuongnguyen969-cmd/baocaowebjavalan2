package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_variants")
public class ProductVariant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "product_id", nullable = false) public Product product;
    @Column(nullable = false, unique = true, length = 50) public String sku;
    @Column(nullable = false, length = 20) public String size;
    @Column(nullable = false, length = 50) public String color;
    @Column(nullable = false, precision = 10, scale = 2) public BigDecimal price;
    @Column(name = "sale_price", precision = 10, scale = 2) public BigDecimal salePrice;
    @Column(nullable = false) public Integer stock = 0;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY) public List<CartItem> cartItems = new ArrayList<>();
    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY) public List<OrderItem> orderItems = new ArrayList<>();
    public ProductVariant() {}
}
