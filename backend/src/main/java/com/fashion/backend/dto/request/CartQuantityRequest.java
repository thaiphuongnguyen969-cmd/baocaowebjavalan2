package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Dữ liệu đổi số lượng một dòng giỏ hàng. */
public record CartQuantityRequest(@NotBlank String sessionId, @NotNull @Positive Integer quantity) {}
