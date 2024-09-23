package com.backend.sade.service;

import com.backend.sade.dto.LoginRequest;
import com.backend.sade.dto.RegisterRequest;
import com.backend.sade.dto.TokenResponse;
import com.backend.sade.entity.User;
import com.backend.sade.repository.UserRepository;
import com.backend.sade.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public TokenResponse login(LoginRequest loginRequest) {
        log.debug("Attempting to authenticate user: {}", loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            log.debug("User {} authenticated successfully", loginRequest.getUsername());
            return new TokenResponse(token);
        } catch (BadCredentialsException ex) {
            log.error("Invalid username or password for user: {}", loginRequest.getUsername());
            throw new BadCredentialsException("Invalid username or password.");
        } catch (Exception ex) {
            log.error("Unexpected error during login for user: {}: {}", loginRequest.getUsername(), ex.getMessage());
            throw new RuntimeException("Unexpected error occurred during login.");
        }
    }

    public void register(RegisterRequest registerRequest) {
        log.debug("Registering new user: {}", registerRequest.getUsername());

        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            log.error("User with email {} already exists", registerRequest.getEmail());
            throw new RuntimeException("User with this email already exists.");
        }
        if (userRepository.findByUsername(registerRequest.getUsername()) != null) {
            log.error("User with username {} already exists", registerRequest.getUsername());
            throw new RuntimeException("This username is already taken.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);
        log.debug("User {} registered successfully", registerRequest.getUsername());
    }
}
