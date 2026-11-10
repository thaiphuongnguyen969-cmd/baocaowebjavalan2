package com.fashion.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/** Dữ liệu cấu hình mã giảm giá. */
public record CouponRequest(@NotBlank String code, @NotBlank String discountType,
                            @NotNull @DecimalMin("0.0") BigDecimal discountValue,
                            @DecimalMin("0.0") BigDecimal minOrderValue,
                            Integer usageLimit, @NotBlank String expiresAt) {
    public Map<String,Object> toMap() {
        Map<String,Object> body=new LinkedHashMap<>();
        body.put("code",code); body.put("discountType",discountType); body.put("discountValue",discountValue);
        body.put("minOrderValue",minOrderValue); body.put("usageLimit",usageLimit); body.put("expiresAt",expiresAt);
        return body;
    }
}
