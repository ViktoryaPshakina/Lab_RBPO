package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Pet;
import com.example.vetclinic.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @PostMapping
    public Pet create(@RequestBody Pet pet) {
        return petRepository.save(pet);
    }

    @GetMapping
    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pet getById(@PathVariable Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
    }

    @PutMapping("/{id}")
    public Pet update(@PathVariable Long id, @RequestBody Pet petDetails) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        pet.setName(petDetails.getName());
        pet.setSpecies(petDetails.getSpecies());
        pet.setOwner(petDetails.getOwner());
        return petRepository.save(pet);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        petRepository.deleteById(id);
    }
}