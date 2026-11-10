package com.fashion.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record CheckoutRequest(
    Integer userId,
    @NotBlank String receiverName,
    @NotBlank String receiverPhone,
    @NotBlank String receiverAddress,
    @Pattern(regexp = "COD|VNPAY|MOMO|BANK_TRANSFER")
    String paymentMethod,
    String couponCode,
    String note,
    @NotEmpty List<@Valid CheckoutItemRequest> items
) {
    public Map<String,Object> toMap() {
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("userId",userId);
        result.put("receiverName",receiverName);
        result.put("receiverPhone",receiverPhone);
        result.put("receiverAddress",receiverAddress);
        result.put("paymentMethod",paymentMethod);
        result.put("couponCode",couponCode);
        result.put("note",note);
        List<Map<String,Object>> rows=new ArrayList<>();
        if(items!=null) for(CheckoutItemRequest item:items) rows.add(item.toMap());
        result.put("items",rows);
        return result;
    }

    public record CheckoutItemRequest(@NotNull @Positive Integer variantId, @NotNull @Positive Integer quantity) {
        Map<String,Object> toMap() {
            Map<String,Object> result=new LinkedHashMap<>();
            result.put("variantId",variantId);
            result.put("quantity",quantity);
            return result;
        }
    }
}
