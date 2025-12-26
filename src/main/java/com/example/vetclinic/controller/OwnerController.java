package com.example.vetclinic.controller;

import com.example.vetclinic.dto.OwnerWithPetRequest;
import com.example.vetclinic.entity.Owner;
import com.example.vetclinic.repository.OwnerRepository;
import com.example.vetclinic.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OwnerController {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ClinicService clinicService;

    @GetMapping("/owners/my-pets")
    public ResponseEntity<?> getMyPets(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Вы не авторизованы");
        }

        String username = principal.getName();
        Optional<Owner> ownerOpt = ownerRepository.findByEmail(username);

        if (ownerOpt.isPresent()) {
            // Возвращаем список питомцев этого владельца
            return ResponseEntity.ok(ownerOpt.get().getPets());
        } else {
            return ResponseEntity.status(404).body("Профиль владельца с email/логином '" + username + "' не найден.");
        }
    }

    @PostMapping("/owners")
    public ResponseEntity<?> createOwner(@RequestBody Owner owner) {
        return ResponseEntity.ok(ownerRepository.save(owner));
    }

    @GetMapping("/owners")
    public ResponseEntity<?> getAllOwners() {
        return ResponseEntity.ok(ownerRepository.findAll());
    }

    @PostMapping("/owners/register-with-pet")
    public ResponseEntity<?> registerOwnerWithPet(@RequestBody OwnerWithPetRequest request) {
        return ResponseEntity.ok(clinicService.registerOwnerWithPet(
                request.getFirstName(), request.getLastName(), request.getPhone(),
                request.getEmail(), request.getPetName(), request.getSpecies(), request.getAge()
        ));
    }

    @DeleteMapping("/owners/{ownerId}")
    public ResponseEntity<String> deleteOwnerCascade(@PathVariable Long ownerId) {
        return ResponseEntity.ok(clinicService.deleteOwnerCascade(ownerId));
    }
}