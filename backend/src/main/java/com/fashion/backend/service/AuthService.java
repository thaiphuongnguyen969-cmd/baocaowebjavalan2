package com.fashion.backend.service;

import com.fashion.backend.dto.request.LoginRequest;
import com.fashion.backend.dto.request.RegisterRequest;
import com.fashion.backend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void register(RegisterRequest request);
}
