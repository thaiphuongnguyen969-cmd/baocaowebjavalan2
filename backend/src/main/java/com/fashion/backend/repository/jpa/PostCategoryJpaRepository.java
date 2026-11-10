package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PostCategoryJpaRepository extends JpaRepository<PostCategory, Integer> {
    List<PostCategory> findAllByOrderByIdAsc();
    List<PostCategory> findAllByOrderByIdDesc();
}
