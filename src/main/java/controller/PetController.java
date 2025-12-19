package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Owner;
import com.example.vetclinic.entity.Pet;
import com.example.vetclinic.repository.OwnerRepository;
import com.example.vetclinic.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        if (pet.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Long ownerId = pet.getOwner().getId();
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + ownerId));

        pet.setOwner(owner);
        Pet saved = petRepository.save(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return ResponseEntity.ok(petRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPet(@PathVariable Long id) {
        return petRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petDetails) {
        return petRepository.findById(id)
                .map(pet -> {
                    pet.setName(petDetails.getName());
                    pet.setSpecies(petDetails.getSpecies());
                    pet.setAge(petDetails.getAge());

                    Long newOwnerId = petDetails.getOwner().getId();
                    Owner newOwner = ownerRepository.findById(newOwnerId)
                            .orElseThrow(() -> new RuntimeException("Owner not found with id: " + newOwnerId));
                    pet.setOwner(newOwner);

                    Pet updated = petRepository.save(pet);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        if (!petRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        petRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}