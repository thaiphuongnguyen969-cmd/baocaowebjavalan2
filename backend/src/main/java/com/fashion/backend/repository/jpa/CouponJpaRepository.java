package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface CouponJpaRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByCodeIgnoreCase(String code);
    List<Coupon> findAllByOrderByIdDesc();
    @Query("select c from Coupon c where c.expiresAt > :now order by c.id desc")
    List<Coupon> findActive(LocalDateTime now);
}
