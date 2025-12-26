package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Treatment;
import com.example.vetclinic.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TreatmentController {

    @Autowired
    private TreatmentRepository treatmentRepository;

    @PostMapping("/treatments")
    public ResponseEntity<?> createTreatment(@RequestBody Treatment treatment) {
        try {
            return ResponseEntity.ok(treatmentRepository.save(treatment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping("/treatments")
    public ResponseEntity<List<Treatment>> getAllTreatments() {
        return ResponseEntity.ok(treatmentRepository.findAll());
    }

    @GetMapping("/treatments/{id}")
    public ResponseEntity<Treatment> getTreatmentById(@PathVariable Long id) {
        return treatmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/treatments/{id}")
    public ResponseEntity<?> updateTreatment(@PathVariable Long id, @RequestBody Treatment treatmentDetails) {
        return treatmentRepository.findById(id)
                .map(treatment -> {
                    treatment.setPrescription(treatmentDetails.getPrescription());
                    treatment.setMedications(treatmentDetails.getMedications());
                    treatment.setFollowUpDate(treatmentDetails.getFollowUpDate());
                    treatment.setAppointment(treatmentDetails.getAppointment());
                    return ResponseEntity.ok(treatmentRepository.save(treatment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/treatments/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long id) {
        if (treatmentRepository.existsById(id)) {
            treatmentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}