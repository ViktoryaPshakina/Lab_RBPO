package com.example.vetclinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    // Поле для телефона (может быть пустым в JSON, но проверим имя)
    private String phone;

    // ВАЖНО: Мапим поле Java на колонку 'specialty' в БД
    @Column(name = "specialty", nullable = false)
    private String specialty;
}