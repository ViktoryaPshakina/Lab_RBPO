package com.example.vetclinic.service;

import com.example.vetclinic.dto.JwtResponse;
import com.example.vetclinic.entity.UserSession;
import com.example.vetclinic.repository.UserSessionRepository;
import com.example.vetclinic.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider tokenProvider;
    private final UserSessionRepository sessionRepository;

    @Transactional
    public JwtResponse createSession(Authentication auth) {
        String email = auth.getName();
        String accessToken = tokenProvider.generateAccessToken(auth);
        String refreshToken = tokenProvider.generateRefreshToken(auth);

        // Сохраняем сессию в БД
        saveSession(email, refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }

    @Transactional
    public JwtResponse refreshSession(String oldRefreshToken) {
        // 1. Ищем старую сессию в БД
        UserSession session = sessionRepository.findByRefreshToken(oldRefreshToken)
                .orElseThrow(() -> new RuntimeException("Сессия не найдена или невалидна"));

        // 2. Валидируем токен
        if (!tokenProvider.validateToken(oldRefreshToken)) {
            sessionRepository.delete(session);
            throw new RuntimeException("Refresh токен просрочен");
        }

        // 3. Генерируем новую пару (Rotation)
        String email = session.getEmail();
        String newAccessToken = tokenProvider.generateAccessTokenFromEmail(email);
        String newRefreshToken = tokenProvider.generateRefreshTokenFromEmail(email);

        // 4. Удаляем старую сессию и сохраняем новую
        sessionRepository.delete(session);
        saveSession(email, newRefreshToken);

        return new JwtResponse(newAccessToken, newRefreshToken);
    }

    public List<UserSession> getUserSessions(String email) {
        return sessionRepository.findAllByEmail(email);
    }

    private void saveSession(String email, String refreshToken) {
        UserSession session = UserSession.builder()
                .email(email)
                .refreshToken(refreshToken)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7)) // Срок как в настройках JWT
                .build();
        sessionRepository.save(session);
    }
}