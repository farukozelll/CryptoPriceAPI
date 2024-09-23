package com.backend.sade.controller;

import com.backend.sade.dto.LoginRequest;
import com.backend.sade.dto.RegisterRequest;
import com.backend.sade.dto.TokenResponse;
import com.backend.sade.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request received for username: {}", loginRequest.getUsername());
        try {
            TokenResponse tokenResponse = authService.login(loginRequest);
            log.info("Login successful for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok(tokenResponse);
        } catch (BadCredentialsException ex) {
            log.error("Invalid credentials for username: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            log.error("An error occurred during login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        log.info("Register request received for username: {}", registerRequest.getUsername());
        authService.register(registerRequest);
        log.info("User registered successfully with username: {}", registerRequest.getUsername());
        return ResponseEntity.ok("User registered successfully");
    }
}
