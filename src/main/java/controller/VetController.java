package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Vet;
import com.example.vetclinic.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vets")
public class VetController {

    @Autowired
    private VetRepository vetRepository;

    @PostMapping
    public Vet create(@RequestBody Vet vet) {
        return vetRepository.save(vet);
    }

    @GetMapping
    public List<Vet> getAll() {
        return vetRepository.findAll();
    }

    @GetMapping("/{id}")
    public Vet getById(@PathVariable Long id) {
        return vetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vet not found"));
    }

    @PutMapping("/{id}")
    public Vet update(@PathVariable Long id, @RequestBody Vet vetDetails) {
        Vet vet = vetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vet not found"));
        vet.setName(vetDetails.getName());
        vet.setSpecialization(vetDetails.getSpecialization());
        return vetRepository.save(vet);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        vetRepository.deleteById(id);
    }
}