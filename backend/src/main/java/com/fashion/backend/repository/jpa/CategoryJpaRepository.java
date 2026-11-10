package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CategoryJpaRepository extends JpaRepository<Category, Integer> {
    List<Category> findAllByOrderByIdAsc();
    List<Category> findByParentIdOrderByIdAsc(Integer parentId);
}
