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

    @PostMapping("/owners")
    public ResponseEntity<?> createOwner(@RequestBody Owner owner) {
        return ResponseEntity.ok(ownerRepository.save(owner));
    }

    @GetMapping("/owners")
    public ResponseEntity<?> getAllOwners() {
        return ResponseEntity.ok(ownerRepository.findAll());
    }

    @GetMapping("/owners/{id}")
    public ResponseEntity<?> getOwnerById(@PathVariable Long id) {
        return ownerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/owners/register-with-pet")
    public ResponseEntity<?> registerOwnerWithPet(@RequestBody OwnerWithPetRequest request) {
        // Передаем весь объект request целиком
        return ResponseEntity.ok(clinicService.registerOwnerWithPet(request));
    }

    @DeleteMapping("/owners/{ownerId}")
    public ResponseEntity<String> deleteOwnerCascade(@PathVariable Long ownerId) {
        return ResponseEntity.ok(clinicService.deleteOwnerCascade(ownerId));
    }
}