package com.backend.sade.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    // Filtreleme dışında tutulacak URL'ler
    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register"
    );
    //Bu metod, belirtilen URL'ler için filtrelemenin yapılmamasını sağlar.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_URLS.contains(request.getRequestURI());
    }
    //Her bir HTTP isteğinde JWT doğrulaması yapar.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwtToken = extractJwtFromRequest(request);

            if (isValidJwt(jwtToken)) {
                setAuthenticationContext(jwtToken, request);
            }
        } catch (Exception ex) {
            log.error("Failed to set user authentication: {}", ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
    //HTTP isteğinden JWT token'ı çıkarır.
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.warn("Invalid Authorization header format for request: {}", request.getRequestURI());
        return null;
    }
    //Verilen JWT token'ın geçerli olup olmadığını kontrol eder.
    private boolean isValidJwt(String jwtToken) {
        return StringUtils.hasText(jwtToken) && tokenProvider.validateToken(jwtToken);
    }
    //JWT token'a dayalı kullanıcı kimlik doğrulama bağlamını ayarlar.
    private void setAuthenticationContext(String jwtToken, HttpServletRequest request) {
        String username = tokenProvider.extractUsernameFromToken(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
            log.warn("User not found with username: {}", username);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User {} successfully authenticated for request: {}", username, request.getRequestURI());
    }
}
