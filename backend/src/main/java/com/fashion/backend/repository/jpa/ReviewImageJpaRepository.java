package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReviewImageJpaRepository extends JpaRepository<ReviewImage, Integer> {
    List<ReviewImage> findByReviewId(Integer reviewId);
    List<ReviewImage> findAllByOrderByIdDesc();
}
