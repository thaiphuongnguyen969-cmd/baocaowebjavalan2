package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/** Dữ liệu đăng nhập; hỗ trợ email hoặc tên tài khoản cũ. */
public record LoginRequest(String email, String username, String account,
                           @NotBlank(message = "Mật khẩu không được để trống") String password) {}
