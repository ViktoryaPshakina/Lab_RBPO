package com.example.vetclinic.controller;

import com.example.vetclinic.dto.JwtResponse;
import com.example.vetclinic.entity.User;
import com.example.vetclinic.repository.UserRepository;
import com.example.vetclinic.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Проверка на существование
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email уже занят"));
        }

        // Хешируем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Устанавливаем роль по умолчанию, если не пришла
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        } else if (!user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole().toUpperCase());
        }

        userRepository.save(user);
        System.out.println("DEBUG: Пользователь " + user.getEmail() + " успешно зарегистрирован");
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.get("email"),
                            loginRequest.get("password")
                    )
            );
            return ResponseEntity.ok(tokenService.createSession(auth));
        } catch (Exception e) {
            System.out.println("DEBUG: Ошибка входа для " + loginRequest.get("email") + ": " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("error", "Неверный email или пароль"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(tokenService.refreshSession(request.get("refreshToken")));
    }

    @GetMapping("/sessions")
    public ResponseEntity<?> getSessions(Authentication authentication) {
        // Получаем email текущего пользователя из контекста безопасности
        String email = authentication.getName();

        // Получаем все сессии этого пользователя из базы через TokenService
        return ResponseEntity.ok(tokenService.getUserSessions(email));
    }

}