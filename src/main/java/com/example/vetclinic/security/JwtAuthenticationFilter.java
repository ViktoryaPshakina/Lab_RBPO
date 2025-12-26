package com.example.vetclinic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // ДИАГНОСТИКА 1: Проверяем наличие заголовка
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("DEBUG: Заголовок Authorization отсутствует или не начинается с Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        System.out.println("DEBUG: Токен получен, проверяем валидность...");

        try {
            if (tokenProvider.validateToken(token)) {
                String email = tokenProvider.getEmailFromToken(token);
                System.out.println("DEBUG: Токен валиден для пользователя: " + email);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("DEBUG: Пользователь успешно аутентифицирован в SecurityContext!");
                }
            } else {
                System.out.println("DEBUG: ОШИБКА - Токен не прошел валидацию (возможно, истек или другой секретный ключ)");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: КРИТИЧЕСКАЯ ОШИБКА в фильтре: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}