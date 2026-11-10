package com.fashion.backend.dto.response;

import java.util.Map;

public record AuthResponse(String token, Map<String, Object> user) {}
