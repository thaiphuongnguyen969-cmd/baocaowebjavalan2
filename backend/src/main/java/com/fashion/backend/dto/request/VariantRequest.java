package com.fashion.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public record VariantRequest(
    @JsonAlias("product_id") @NotNull @Positive Integer productId,
    @NotBlank String sku,
    @NotBlank String size,
    @NotBlank String color,
    @NotNull @DecimalMin("0.0") BigDecimal price,
    @JsonAlias("sale_price") BigDecimal salePrice,
    @PositiveOrZero Integer stock
) {
    public Map<String,Object> toMap() {
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("productId",productId);
        result.put("sku",sku);
        result.put("size",size);
        result.put("color",color);
        result.put("price",price);
        result.put("salePrice",salePrice);
        result.put("stock",stock);
        return result;
    }
}
