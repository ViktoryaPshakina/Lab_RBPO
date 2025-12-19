package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Appointment;
import com.example.vetclinic.entity.Treatment;
import com.example.vetclinic.repository.AppointmentRepository;
import com.example.vetclinic.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public ResponseEntity<Treatment> createTreatment(@RequestBody Treatment treatment) {
        if (treatment.getId() != null) {
            return ResponseEntity.badRequest().build();
        }

        Long appointmentId = treatment.getAppointment().getId();
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Проверка: лечение уже есть?
        if (treatmentRepository.existsByAppointmentId(appointmentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null); // или выбросить исключение — зависит от требований
        }

        treatment.setAppointment(appointment);
        Treatment saved = treatmentRepository.save(treatment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Treatment>> getAllTreatments() {
        return ResponseEntity.ok(treatmentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treatment> getTreatment(@PathVariable Long id) {
        return treatmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Treatment> updateTreatment(@PathVariable Long id, @RequestBody Treatment details) {
        return treatmentRepository.findById(id)
                .map(treatment -> {
                    treatment.setDescription(details.getDescription());
                    treatment.setMedications(details.getMedications());

                    // Связь appointment — нельзя менять после создания (по ТЗ)
                    // Если нужно — можно разрешить, но осторожно

                    Treatment updated = treatmentRepository.save(treatment);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long id) {
        if (!treatmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        treatmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}