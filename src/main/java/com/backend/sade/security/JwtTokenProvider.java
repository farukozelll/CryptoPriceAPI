package com.backend.sade.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey jwtSecretKey; // JWT imzalama anahtarı
    private final int jwtExpirationMs; // JWT geçerlilik süresi (milisaniye)

    // JWT imzalama anahtarı ve geçerlilik süresi ile birlikte JwtTokenProvider sınıfını başlatır.
    public JwtTokenProvider(
            @Value("${app.jwt.jwtSecret}") String jwtSecret,
            @Value("${app.jwt.jwtExpirationMs}") int jwtExpirationMs) {

        if (jwtSecret == null || jwtSecret.length() < 64) {
            throw new IllegalArgumentException("JWT Secret must be at least 64 characters long.");
        }
        if (jwtExpirationMs <= 0) {
            throw new IllegalArgumentException("JWT Expiration time must be positive.");
        }

        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }
    //Verilen kimlik doğrulama bilgilerini kullanarak bir JWT token oluşturur.
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        //// JWT token oluşturulur ve imzalanır
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    //Verilen JWT token'dan kullanıcı adını (username) çıkarır.
    public String extractUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }
    //Verilen JWT token'ın geçerli olup olmadığını kontrol eder.
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

   //Verilen JWT token içindeki tüm bilgileri çıkarır.
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
