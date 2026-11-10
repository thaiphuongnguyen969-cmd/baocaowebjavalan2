package com.fashion.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Thay đổi mật khẩu cần xác thực mật khẩu hiện tại. */
public record ChangePasswordRequest(@NotBlank String currentPassword,
                                    @NotBlank @Size(min = 6, max = 72) String newPassword) {}
