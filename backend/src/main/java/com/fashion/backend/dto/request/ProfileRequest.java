package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/** Các trường hồ sơ mà người dùng được phép tự cập nhật. */
public record ProfileRequest(@NotBlank String fullname, @NotBlank String email,
                             String phone, String address) {}
