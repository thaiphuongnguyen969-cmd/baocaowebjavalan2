package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySlug(String slug);
    @Query("""
        select distinct p from Product p left join p.variants v
        where p.active = true
          and (:categoryId is null or p.category.id = :categoryId)
          and (:search is null or lower(p.name) like lower(concat('%', :search, '%')))
          and (:minPrice is null or coalesce(v.salePrice, v.price) >= :minPrice)
          and (:maxPrice is null or coalesce(v.salePrice, v.price) <= :maxPrice)
        """)
    Page<Product> search(@Param("search") String search, @Param("categoryId") Integer categoryId,
                         @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice,
                         Pageable pageable);
}
