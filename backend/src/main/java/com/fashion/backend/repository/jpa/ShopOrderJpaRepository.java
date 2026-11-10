package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
public interface ShopOrderJpaRepository extends JpaRepository<ShopOrder, Integer> {
    Optional<ShopOrder> findByOrderCode(String orderCode);
    List<ShopOrder> findByUserIdOrderByCreatedAtDesc(Integer userId);
    List<ShopOrder> findAllByOrderByCreatedAtDesc();
    @Query("select coalesce(sum(o.finalAmount), 0) from ShopOrder o where o.paymentStatus = 'paid'")
    java.math.BigDecimal paidRevenue();
}
