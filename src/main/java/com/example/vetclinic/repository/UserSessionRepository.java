package com.example.vetclinic.repository;

import com.example.vetclinic.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByRefreshToken(String refreshToken);
    List<UserSession> findAllByEmail(String email);
    void deleteByRefreshToken(String refreshToken);
    void deleteByEmail(String email); // Для логаута (удалить все сессии пользователя)
}