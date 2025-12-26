package com.example.vetclinic.repository;

import com.example.vetclinic.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    // Этот метод нужен, чтобы найти владельца по логину (email)
    Optional<Owner> findByEmail(String email);
}