package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PostCommentJpaRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findByPostIdOrderByCreatedAtDesc(Integer postId);
    List<PostComment> findAllByOrderByCreatedAtDesc();
}
