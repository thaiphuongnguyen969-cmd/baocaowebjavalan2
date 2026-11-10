package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface CartItemJpaRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartIdAndVariantId(Integer cartId, Integer variantId);
    List<CartItem> findByCartSessionId(String sessionId);
    Optional<CartItem> findByIdAndCartSessionId(Integer id, String sessionId);
}
