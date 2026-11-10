package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @Column(nullable = false, length = 100) public String name;
    @Column(nullable = false, unique = true, length = 100) public String slug;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_id") public Category parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY) public List<Category> children = new ArrayList<>();
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY) public List<Product> products = new ArrayList<>();
    @Column(columnDefinition = "TEXT") public String description;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    public Category() {}
}
