package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "post_id", nullable = false) public Post post;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) public User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_id") public PostComment parent;
    @Column(nullable = false, columnDefinition = "TEXT") public String content;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY) public List<PostComment> replies = new ArrayList<>();
    public PostComment() {}
}
