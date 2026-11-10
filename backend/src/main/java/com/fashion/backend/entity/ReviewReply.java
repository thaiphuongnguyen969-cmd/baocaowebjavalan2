package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_replies")
public class ReviewReply {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "review_id", nullable = false, unique = true) public Review review;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) public User user;
    @Column(nullable = false, columnDefinition = "TEXT") public String comment;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    public ReviewReply() {}
}
