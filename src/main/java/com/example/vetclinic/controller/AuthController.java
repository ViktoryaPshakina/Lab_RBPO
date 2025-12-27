package com.example.vetclinic.controller;

import com.example.vetclinic.entity.User;
import com.example.vetclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Проверка пароля
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        if (user.getPassword() == null || !user.getPassword().matches(regex)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Слабый пароль!",
                    "message", "Требуется 8+ символов, цифры, заглавные буквы и спецсимволы."
            ));
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username занят"));
        }

        // Хеширование и сохранение
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of("ROLE_USER"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Регистрация успешна"));
    }
}