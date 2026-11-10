package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    @Column(nullable = false, unique = true, length = 50)
    public String code;
    @Column(name = "discount_type", nullable = false, length = 20)
    public String discountType;
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    public BigDecimal discountValue;
    @Column(name = "min_order_value", precision = 10, scale = 2)
    public BigDecimal minOrderValue;
    @Column(name = "usage_limit")
    public Integer usageLimit;
    @Column(name = "used_count")
    public Integer usedCount;
    @Column(name = "expires_at", nullable = false)
    public LocalDateTime expiresAt;
    @Column(name = "created_at", insertable = false, updatable = false)
    public LocalDateTime createdAt;

    public Coupon() {
    }
}
