package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Dữ liệu đăng ký tài khoản khách hàng. */
public record RegisterRequest(@NotBlank(message = "Họ tên không được để trống") String fullname,
                              String email, String username, String account,
                              @NotBlank(message = "Mật khẩu không được để trống")
                              @Size(min = 6, max = 72, message = "Mật khẩu phải có từ 6 đến 72 ký tự")
                              String password, String phone, String address) {}
