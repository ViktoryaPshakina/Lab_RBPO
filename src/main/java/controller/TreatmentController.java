package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Treatment;
import com.example.vetclinic.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatments")
public class TreatmentController {

    @Autowired
    private TreatmentRepository treatmentRepository;

    @PostMapping
    public Treatment create(@RequestBody Treatment treatment) {
        return treatmentRepository.save(treatment);
    }

    @GetMapping
    public List<Treatment> getAll() {
        return treatmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Treatment getById(@PathVariable Long id) {
        return treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found"));
    }

    @PutMapping("/{id}")
    public Treatment update(@PathVariable Long id, @RequestBody Treatment treatmentDetails) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found"));
        treatment.setAppointment(treatmentDetails.getAppointment());
        treatment.setDescription(treatmentDetails.getDescription());
        return treatmentRepository.save(treatment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        treatmentRepository.deleteById(id);
    }
}