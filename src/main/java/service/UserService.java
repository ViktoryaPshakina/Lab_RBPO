package com.example.vetclinic.service;

import com.example.vetclinic.entity.User;
import com.example.vetclinic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String password, String email, Set<String> roles) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Имя пользователя уже занято");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email уже используется");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new RuntimeException("Имя пользователя обязательно");
        }
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Пароль должен быть не менее 8 символов");
        }
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            throw new RuntimeException("Пароль должен содержать буквы и цифры");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new RuntimeException("Некорректный email");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}