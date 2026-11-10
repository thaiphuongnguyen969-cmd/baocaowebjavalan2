package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "category_id", nullable = false) public PostCategory category;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) public User author;
    @Column(nullable = false, length = 200) public String title;
    @Column(nullable = false, unique = true, length = 200) public String slug;
    @Column(length = 255) public String thumbnail;
    @Column(columnDefinition = "TEXT") public String summary;
    @Column(nullable = false, columnDefinition = "LONGTEXT") public String content;
    @Column public Integer views = 0;
    @Column(name = "is_published") public Boolean published = true;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false) public LocalDateTime updatedAt;
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY) public List<PostComment> comments = new ArrayList<>();
    public Post() {}
}
