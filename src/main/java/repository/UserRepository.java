package com.example.vetclinic.repository;

import com.example.vetclinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Поиск по логину
    Optional<User> findByUsername(String username);

    // Поиск по email (именно этого метода не хватало UserService)
    Optional<User> findByEmail(String email);

    // Проверка существования (для AuthController)
    boolean existsByUsername(String username);
}