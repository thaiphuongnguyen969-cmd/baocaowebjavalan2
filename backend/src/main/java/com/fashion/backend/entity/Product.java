package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "category_id", nullable = false) public Category category;
    @Column(nullable = false, length = 150) public String name;
    @Column(nullable = false, unique = true, length = 150) public String slug;
    @Column(columnDefinition = "TEXT") public String description;
    @Column(length = 255) public String thumbnail;
    @Column(columnDefinition = "json") public String images;
    @Column(name = "is_active") public Boolean active = true;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false) public LocalDateTime updatedAt;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY) public List<ProductVariant> variants = new ArrayList<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY) public List<Review> reviews = new ArrayList<>();
    public Product() {}
}
