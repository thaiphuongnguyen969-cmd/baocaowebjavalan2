package com.fashion.backend.controller;

import com.fashion.backend.service.UserService;
import com.fashion.backend.dto.request.ProfileRequest;
import com.fashion.backend.dto.request.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** API hồ sơ cá nhân của tài khoản đã đăng nhập. */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) { this.userService=userService; }

    @GetMapping("/me")
    public Map<String,Object> profile(Authentication authentication) {
        return userService.profile(authentication.getName());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Authentication authentication,@Valid @RequestBody ProfileRequest request) {
        userService.updateProfile(authentication.getName(),Map.of("fullname",request.fullname(),"email",request.email(),
            "phone",request.phone()==null?"":request.phone(),"address",request.address()==null?"":request.address()));
        return ResponseEntity.ok(Map.of("message","Cập nhật hồ sơ thành công"));
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(Authentication authentication,@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(authentication.getName(),Map.of("currentPassword",request.currentPassword(),
            "newPassword",request.newPassword()));
        return ResponseEntity.ok(Map.of("message","Đổi mật khẩu thành công"));
    }
}
