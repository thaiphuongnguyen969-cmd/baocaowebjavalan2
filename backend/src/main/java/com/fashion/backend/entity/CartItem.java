package com.fashion.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "cart_id", nullable = false) public Cart cart;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "product_variant_id", nullable = false) public ProductVariant variant;
    @Column(nullable = false) public Integer quantity = 1;
    public CartItem() {}
}
