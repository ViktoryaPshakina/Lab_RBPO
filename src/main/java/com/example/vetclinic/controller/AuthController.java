package com.example.vetclinic.controller;

import com.example.vetclinic.dto.JwtResponse;
import com.example.vetclinic.dto.RegisterRequest;
import com.example.vetclinic.service.TokenService;
import com.example.vetclinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody Map<String, String> loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.get("email"), loginRequest.get("password"))
        );
        return ResponseEntity.ok(tokenService.createSession(authentication));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        // Здесь мы передаем null в authentication, так как TokenService должен уметь
        // восстановить данные пользователя из самого токена или БД.
        // Если твой TokenService требует Authentication, его нужно создать из email в токене.
        return ResponseEntity.ok(tokenService.refreshSession(refreshToken, null));
    }
}