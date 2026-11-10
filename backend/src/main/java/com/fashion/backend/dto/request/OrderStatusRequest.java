package com.fashion.backend.dto.request;

import jakarta.validation.constraints.Pattern;

import java.util.LinkedHashMap;
import java.util.Map;

public record OrderStatusRequest(
    @Pattern(regexp = "pending|confirmed|shipping|completed|cancelled")
    String orderStatus,
    @Pattern(regexp = "pending|paid|refunded")
    String paymentStatus
) {
    public Map<String, String> toMap() {
        Map<String, String> result = new LinkedHashMap<>();
        if (orderStatus != null) result.put("orderStatus", orderStatus);
        if (paymentStatus != null) result.put("paymentStatus", paymentStatus);
        return result;
    }
}
