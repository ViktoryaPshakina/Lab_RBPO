package com.example.vetclinic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    private Vet vet;

    private LocalDateTime appointmentTime;
    private String status; // "SCHEDULED", "COMPLETED"

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Treatment treatment;
}