package com.example.vetclinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String specialty;
    private String phone;
}