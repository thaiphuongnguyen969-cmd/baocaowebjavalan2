package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface PostJpaRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findBySlug(String slug);
    Page<Post> findByPublishedTrue(Pageable pageable);
    java.util.List<Post> findAllByOrderByIdDesc();
}
