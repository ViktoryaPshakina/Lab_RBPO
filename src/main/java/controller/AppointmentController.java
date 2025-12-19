package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Appointment;
import com.example.vetclinic.entity.Pet;
import com.example.vetclinic.entity.Vet;
import com.example.vetclinic.repository.AppointmentRepository;
import com.example.vetclinic.repository.PetRepository;
import com.example.vetclinic.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VetRepository vetRepository;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        if (appointment.getId() != null) {
            return ResponseEntity.badRequest().build();
        }

        Long petId = appointment.getPet().getId();
        Long vetId = appointment.getVet().getId();

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new RuntimeException("Vet not found with id: " + vetId));

        appointment.setPet(pet);
        appointment.setVet(vet);
        // Убедись, что дата установлена (иначе может быть null)
        if (appointment.getDateTime() == null) {
            appointment.setDateTime(LocalDateTime.now());
        }

        Appointment saved = appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment details) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setDateTime(details.getDateTime());
                    appointment.setReason(details.getReason());

                    Long newPetId = details.getPet().getId();
                    Long newVetId = details.getVet().getId();
                    Pet newPet = petRepository.findById(newPetId)
                            .orElseThrow(() -> new RuntimeException("Pet not found: " + newPetId));
                    Vet newVet = vetRepository.findById(newVetId)
                            .orElseThrow(() -> new RuntimeException("Vet not found: " + newVetId));

                    appointment.setPet(newPet);
                    appointment.setVet(newVet);

                    Appointment updated = appointmentRepository.save(appointment);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (!appointmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        appointmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}