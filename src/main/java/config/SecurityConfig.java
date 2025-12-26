package com.example.vetclinic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // 1. Открытые пути
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/owners/register-with-pet").permitAll()

                        // 2. Личный кабинет (ВАЖНО: выше чем /api/owners/**)
                        .requestMatchers("/api/owners/my-pets").hasAnyRole("USER", "ADMIN")

                        // 3. Только для АДМИНА
                        .requestMatchers("/api/owners/**").hasRole("ADMIN")
                        .requestMatchers("/api/vets/**").hasRole("ADMIN")

                        // 4. Доступ по логину для всех ролей
                        .requestMatchers("/api/pets/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/appointments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/treatments/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(h -> h.realmName("VetClinic"))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}