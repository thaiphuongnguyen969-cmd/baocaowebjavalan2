package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Dữ liệu thêm sản phẩm vào giỏ khách hoặc tài khoản. */
public record CartItemRequest(@NotBlank String sessionId, @NotNull @Positive Integer variantId,
                              @NotNull @Positive Integer quantity) {}
