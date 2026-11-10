package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) public User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "product_id", nullable = false) public Product product;
    @Column(nullable = false) public Integer rating;
    @Column(columnDefinition = "TEXT") public String comment;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY) public List<ReviewImage> images = new ArrayList<>();
    @OneToOne(mappedBy = "review", fetch = FetchType.LAZY) public ReviewReply reply;
    public Review() {}
}
