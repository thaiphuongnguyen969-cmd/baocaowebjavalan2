package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
public interface ProductVariantJpaRepository extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> findByProductIdOrderByPriceAscIdAsc(Integer productId);
    List<ProductVariant> findAllByOrderByIdDesc();
    List<ProductVariant> findByProductIdOrderByIdAsc(Integer productId);
    List<ProductVariant> findByStockLessThanOrderByStockAsc(Integer stock);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from ProductVariant v join fetch v.product where v.id = :id")
    Optional<ProductVariant> findForCheckout(@Param("id") Integer id);
    @Modifying
    @Query("update ProductVariant v set v.stock = v.stock - :quantity where v.id = :id and v.stock >= :quantity")
    int decrementStock(@Param("id") Integer id, @Param("quantity") Integer quantity);
}
