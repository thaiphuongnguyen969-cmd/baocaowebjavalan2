package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_images")
public class ReviewImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "review_id", nullable = false) public Review review;
    @Column(name = "image_url", nullable = false, length = 255) public String imageUrl;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    public ReviewImage() {}
}
