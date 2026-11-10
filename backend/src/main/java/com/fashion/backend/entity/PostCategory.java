package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post_categories")
public class PostCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @Column(nullable = false, length = 100) public String name;
    @Column(nullable = false, unique = true, length = 100) public String slug;
    @Column(columnDefinition = "TEXT") public String description;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY) public List<Post> posts = new ArrayList<>();
    public PostCategory() {}
}
