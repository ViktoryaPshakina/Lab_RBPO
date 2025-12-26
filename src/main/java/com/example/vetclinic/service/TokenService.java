package com.example.vetclinic.service;

import com.example.vetclinic.dto.JwtResponse;
import com.example.vetclinic.entity.SessionStatus;
import com.example.vetclinic.entity.UserSession;
import com.example.vetclinic.repository.UserSessionRepository;
import com.example.vetclinic.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider tokenProvider;
    private final UserSessionRepository sessionRepository;

    @Transactional
    public JwtResponse createSession(Authentication authentication) {
        String email = authentication.getName();
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(email);

        UserSession session = UserSession.builder()
                .userEmail(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiry(tokenProvider.getExpirationDate(accessToken).toInstant())
                .refreshTokenExpiry(tokenProvider.getExpirationDate(refreshToken).toInstant())
                .status(SessionStatus.ACTIVE)
                .build();

        sessionRepository.save(session);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Transactional
    public JwtResponse refreshSession(String refreshToken, Authentication authentication) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid Refresh Token");
        }

        UserSession oldSession = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Session not found"));

        if (oldSession.getStatus() == SessionStatus.USED) {
            oldSession.setStatus(SessionStatus.REVOKED);
            sessionRepository.save(oldSession);
            throw new BadCredentialsException("Token reused! All sessions should be revoked for security.");
        }

        if (oldSession.getStatus() == SessionStatus.REVOKED) {
            throw new BadCredentialsException("Session is revoked");
        }

        oldSession.setStatus(SessionStatus.USED);
        sessionRepository.save(oldSession);

        return createSession(authentication);
    }
}