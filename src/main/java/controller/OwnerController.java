package com.example.vetclinic.controller;

import com.example.vetclinic.dto.OwnerWithPetRequest;
import com.example.vetclinic.entity.Owner;
import com.example.vetclinic.repository.OwnerRepository;
import com.example.vetclinic.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OwnerController {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ClinicService clinicService;

    // CRUD: создать владельца
    @PostMapping("/owners")
    public ResponseEntity<?> createOwner(@RequestBody Owner owner) {
        try {
            return ResponseEntity.ok(ownerRepository.save(owner));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    // CRUD: получить всех владельцев
    @GetMapping("/owners")
    public ResponseEntity<?> getAllOwners() {
        return ResponseEntity.ok(ownerRepository.findAll());
    }

    // CRUD: получить владельца по ID
    @GetMapping("/owners/{id}")
    public ResponseEntity<?> getOwnerById(@PathVariable Long id) {
        return ownerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 1. Регистрация владельца с питомцем
    @PostMapping("/owners/register-with-pet")
    public ResponseEntity<?> registerOwnerWithPet(@RequestBody OwnerWithPetRequest request) {
        return ResponseEntity.ok(clinicService.registerOwnerWithPet(
                request.getFirstName(),
                request.getLastName(),
                request.getPhone(),
                request.getEmail(),
                request.getPetName(),
                request.getSpecies(),
                request.getAge()
        ));
    }

    // 4. Удаление владельца с каскадом
    @DeleteMapping("/owners/{ownerId}")
    public ResponseEntity<String> deleteOwnerCascade(@PathVariable Long ownerId) {
        return ResponseEntity.ok(clinicService.deleteOwnerCascade(ownerId));
    }
}