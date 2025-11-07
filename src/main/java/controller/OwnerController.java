package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Owner;
import com.example.vetclinic.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    @Autowired
    private OwnerRepository ownerRepository;

    @PostMapping
    public Owner create(@RequestBody Owner owner) {
        return ownerRepository.save(owner);
    }

    @GetMapping
    public List<Owner> getAll() {
        return ownerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Owner getById(@PathVariable Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
    }

    @PutMapping("/{id}")
    public Owner update(@PathVariable Long id, @RequestBody Owner ownerDetails) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.setName(ownerDetails.getName());
        owner.setPhone(ownerDetails.getPhone());
        return ownerRepository.save(owner);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ownerRepository.deleteById(id);
    }
}