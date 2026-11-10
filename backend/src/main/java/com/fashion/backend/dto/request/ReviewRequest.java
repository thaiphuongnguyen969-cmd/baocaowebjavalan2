package com.fashion.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.LinkedHashMap;
import java.util.Map;

/** Dữ liệu đánh giá sản phẩm. */
public record ReviewRequest(@NotNull @Positive Integer userId, @NotNull @Positive Integer productId,
                            @NotNull @Min(1) @Max(5) Integer rating, String comment) {
    public Map<String,Object> toMap() {
        Map<String,Object> body=new LinkedHashMap<>();
        body.put("userId",userId); body.put("productId",productId); body.put("rating",rating);
        body.put("comment",comment);
        return body;
    }
}
