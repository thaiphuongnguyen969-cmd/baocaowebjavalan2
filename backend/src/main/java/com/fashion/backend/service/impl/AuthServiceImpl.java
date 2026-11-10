package com.fashion.backend.service.impl;

import com.fashion.backend.dto.request.LoginRequest;
import com.fashion.backend.dto.request.RegisterRequest;
import com.fashion.backend.dto.response.AuthResponse;
import com.fashion.backend.entity.User;
import com.fashion.backend.exception.BadRequestException;
import com.fashion.backend.exception.UnauthorizedException;
import com.fashion.backend.repository.UserRepository;
import com.fashion.backend.security.JwtTokenProvider;
import com.fashion.backend.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import com.fashion.backend.util.AccountNormalizer;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String account = first(request.email(), request.username(), request.account());
        String email = normalizeAccount(account);
        String password = request.password() == null ? "" : request.password();
        if (email.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("Vui lòng nhập tài khoản và mật khẩu");
        }

        User user = userRepository.findByEmailIncludingPassword(email);
        if (user == null) throw new UnauthorizedException("Email hoặc mật khẩu không đúng");

        String storedPassword = user.password();
        boolean matches = isBcrypt(storedPassword)
            ? passwordEncoder.matches(password, storedPassword)
            : storedPassword.equals(password);
        if (!Integer.valueOf(1).equals(user.status()) || !matches) {
            throw new UnauthorizedException("Email hoặc mật khẩu không đúng");
        }
        if (!isBcrypt(storedPassword)) {
            userRepository.updatePassword(user.id(), passwordEncoder.encode(password));
        }

        String token = tokenProvider.createToken(user);
        Map<String, Object> safeUser = Map.of(
            "id", user.id(), "fullname", user.fullname(), "email", user.email(), "role", user.role());
        return new AuthResponse(token, safeUser);
    }

    @Override
    public void register(RegisterRequest request) {
        String fullname = request.fullname() == null ? "" : request.fullname().trim();
        String email = normalizeAccount(first(request.email(), request.username(), request.account()));
        String password = request.password() == null ? "" : request.password();
        if (fullname.isEmpty() || email.isEmpty() || password.length() < 6) {
            throw new BadRequestException("Vui lòng nhập đầy đủ họ tên, tài khoản và mật khẩu từ 6 ký tự");
        }
        if (userRepository.emailExists(email)) {
            throw new BadRequestException("Email hoặc tài khoản đã được sử dụng");
        }
        userRepository.create(fullname, email, passwordEncoder.encode(password),
            request.phone() == null ? "" : request.phone(),
            request.address() == null ? "" : request.address(),
            "customer", 1);
    }

    private String first(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) return value;
        }
        return "";
    }

    private String normalizeAccount(String value) {
        return AccountNormalizer.normalize(value);
    }

    private boolean isBcrypt(String password) {
        return password != null && (
            password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }
}
