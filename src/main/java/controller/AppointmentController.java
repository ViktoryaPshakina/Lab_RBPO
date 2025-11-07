package com.example.vetclinic.controller;

import com.example.vetclinic.entity.Appointment;
import com.example.vetclinic.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @PostMapping
    public Appointment create(@RequestBody Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @GetMapping
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setPet(appointmentDetails.getPet());
        appointment.setVet(appointmentDetails.getVet());
        appointment.setAppointmentTime(appointmentDetails.getAppointmentTime());
        appointment.setStatus(appointmentDetails.getStatus());
        return appointmentRepository.save(appointment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        appointmentRepository.deleteById(id);
    }
}