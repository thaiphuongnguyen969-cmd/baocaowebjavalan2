package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/** Đường dẫn hình ảnh gắn với một đánh giá. */
public record ReviewImageRequest(@NotBlank String imageUrl) {}
