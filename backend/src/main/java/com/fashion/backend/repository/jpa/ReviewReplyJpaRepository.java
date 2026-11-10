package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.ReviewReply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ReviewReplyJpaRepository extends JpaRepository<ReviewReply, Integer> {
    Optional<ReviewReply> findByReviewId(Integer reviewId);
    List<ReviewReply> findAllByOrderByIdDesc();
}
