package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") public User user;
    @Column(name = "session_id", length = 255) public String sessionId;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false) public LocalDateTime updatedAt;
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY) public List<CartItem> items = new ArrayList<>();
    public Cart() {}
}
