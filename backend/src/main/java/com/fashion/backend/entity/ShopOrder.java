package com.fashion.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class ShopOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Integer id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") public User user;
    @Column(name = "order_code", nullable = false, unique = true, length = 50) public String orderCode;
    @Column(name = "receiver_name", nullable = false, length = 100) public String receiverName;
    @Column(name = "receiver_phone", nullable = false, length = 20) public String receiverPhone;
    @Column(name = "receiver_address", nullable = false, columnDefinition = "TEXT") public String receiverAddress;
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2) public BigDecimal totalAmount;
    @Column(name = "discount_amount", precision = 10, scale = 2) public BigDecimal discountAmount;
    @Column(name = "shipping_fee", precision = 10, scale = 2) public BigDecimal shippingFee;
    @Column(name = "final_amount", nullable = false, precision = 10, scale = 2) public BigDecimal finalAmount;
    @Column(name = "payment_method", length = 20) public String paymentMethod;
    @Column(name = "payment_status", length = 20) public String paymentStatus;
    @Column(name = "order_status", length = 20) public String orderStatus;
    @Column(columnDefinition = "TEXT") public String note;
    @Column(name = "created_at", insertable = false, updatable = false) public LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false) public LocalDateTime updatedAt;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY) public List<OrderItem> items = new ArrayList<>();
    public ShopOrder() {}
}
