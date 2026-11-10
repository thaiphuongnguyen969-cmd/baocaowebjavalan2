package com.fashion.backend.security;

import com.fashion.backend.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secret;

    public JwtTokenProvider(@Value("${app.jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String createToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
            .subject(user.email())
            .claim("userId", user.id())
            .claim("role", user.role())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(24 * 60 * 60)))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }
}
