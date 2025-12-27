package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Vet;
import com.example.vetclinic.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VetController {

    @Autowired
    private VetRepository vetRepository;

    // CRUD: создать врача
    @PostMapping("/vets")
    public ResponseEntity<?> createVet(@RequestBody Vet vet) {
        try {
            return ResponseEntity.ok(vetRepository.save(vet));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    // CRUD: получить всех врачей
    @GetMapping("/vets")
    public ResponseEntity<List<Vet>> getAllVets() {
        return ResponseEntity.ok(vetRepository.findAll());
    }

    // CRUD: получить врача по ID
    @GetMapping("/vets/{id}")
    public ResponseEntity<Vet> getVetById(@PathVariable Long id) {
        return vetRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CRUD: обновить врача
    @PutMapping("/vets/{id}")
    public ResponseEntity<?> updateVet(@PathVariable Long id, @RequestBody Vet vetDetails) {
        return vetRepository.findById(id)
                .map(vet -> {
                    vet.setFirstName(vetDetails.getFirstName());
                    vet.setLastName(vetDetails.getLastName());
                    vet.setSpecialty(vetDetails.getSpecialty());
                    vet.setPhone(vetDetails.getPhone());
                    return ResponseEntity.ok(vetRepository.save(vet));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // CRUD: удалить врача
    @DeleteMapping("/vets/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable Long id) {
        if (vetRepository.existsById(id)) {
            vetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}