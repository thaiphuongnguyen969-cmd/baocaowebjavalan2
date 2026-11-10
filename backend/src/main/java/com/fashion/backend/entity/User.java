package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @Column(nullable = false, length = 100) public String fullname;
    @Column(nullable = false, unique = true, length = 100) public String email;
    @Column(nullable = false, length = 255) public String password;
    @Column(length = 20) public String phone;
    @Column(columnDefinition = "TEXT") public String address;
    @Column(nullable = false, length = 20) public String role;
    @Column(nullable = false) public Integer status;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false) public LocalDateTime updatedAt;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) public List<Cart> carts = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) public List<ShopOrder> orders = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) public List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) public List<ReviewReply> reviewReplies = new ArrayList<>();
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY) public List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) public List<PostComment> comments = new ArrayList<>();

    public User() {}

    public User(Integer id, String fullname, String email, String password, String role, Integer status) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Integer id() { return id; }
    public String fullname() { return fullname; }
    public String email() { return email; }
    public String password() { return password; }
    public String role() { return role; }
    public Integer status() { return status; }
}
