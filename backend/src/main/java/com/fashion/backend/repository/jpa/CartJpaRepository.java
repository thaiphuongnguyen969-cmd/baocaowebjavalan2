package com.fashion.backend.repository.jpa;

import com.fashion.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CartJpaRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findFirstBySessionId(String sessionId);
}
