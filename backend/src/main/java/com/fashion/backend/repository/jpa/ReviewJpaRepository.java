package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReviewJpaRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductIdOrderByCreatedAtDesc(Integer productId);
    List<Review> findAllByOrderByCreatedAtDesc();
}
