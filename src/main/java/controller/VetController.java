package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Vet;
import com.example.vetclinic.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vets")
public class VetController {

    @Autowired
    private VetRepository vetRepository;

    @PostMapping
    public ResponseEntity<Vet> createVet(@RequestBody Vet vet) {
        if (vet.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Vet saved = vetRepository.save(vet);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Vet>> getAllVets() {
        return ResponseEntity.ok(vetRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vet> getVet(@PathVariable Long id) {
        return vetRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vet> updateVet(@PathVariable Long id, @RequestBody Vet vetDetails) {
        return vetRepository.findById(id)
                .map(vet -> {
                    vet.setFirstName(vetDetails.getFirstName());
                    vet.setLastName(vetDetails.getLastName());
                    vet.setSpecialty(vetDetails.getSpecialty());
                    Vet updated = vetRepository.save(vet);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable Long id) {
        if (!vetRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}