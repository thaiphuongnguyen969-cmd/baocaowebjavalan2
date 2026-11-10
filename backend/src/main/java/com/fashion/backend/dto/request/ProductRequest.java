package com.fashion.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public record ProductRequest(
    @JsonAlias("category_id") @NotNull @Positive Integer categoryId,
    @NotBlank String name,
    @NotBlank String slug,
    String description,
    String thumbnail,
    Object images,
    @JsonAlias("is_active") Integer isActive,
    String sku,
    String size,
    String color,
    BigDecimal price,
    @JsonAlias("sale_price") BigDecimal salePrice,
    Integer stock
) {
    public Map<String,Object> toMap() {
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("categoryId",categoryId);
        result.put("name",name);
        result.put("slug",slug);
        result.put("description",description);
        result.put("thumbnail",thumbnail);
        result.put("images",images);
        result.put("isActive",isActive);
        result.put("sku",sku);
        result.put("size",size);
        result.put("color",color);
        result.put("price",price);
        result.put("salePrice",salePrice);
        result.put("stock",stock);
        return result;
    }
}
