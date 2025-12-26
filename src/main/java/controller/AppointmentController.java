package com.example.vetclinic.controller;

import com.example.vetclinic.dto.MedicalHistoryDto;
import com.example.vetclinic.dto.VetScheduleDto;
import com.example.vetclinic.entity.Appointment;
import com.example.vetclinic.repository.AppointmentRepository;
import com.example.vetclinic.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClinicService clinicService;

    // CRUD: создать приём
    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            return ResponseEntity.ok(appointmentRepository.save(appointment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    // CRUD: получить всех приёмов
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepository.findAll());
    }

    // CRUD: получить приём по ID
    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CRUD: обновить приём
    @PutMapping("/appointments/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointmentDetails) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setDateTime(appointmentDetails.getDateTime());
                    appointment.setReason(appointmentDetails.getReason());
                    appointment.setComplaints(appointmentDetails.getComplaints());
                    appointment.setStatus(appointmentDetails.getStatus());
                    appointment.setPet(appointmentDetails.getPet());
                    appointment.setVet(appointmentDetails.getVet());
                    return ResponseEntity.ok(appointmentRepository.save(appointment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // CRUD: удалить приём
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ 2. История болезни питомца
    @GetMapping("/pets/{petId}/medical-history")
    public ResponseEntity<List<MedicalHistoryDto>> getMedicalHistory(@PathVariable Long petId) {
        return ResponseEntity.ok(clinicService.getMedicalHistory(petId));
    }

    // ✅ 3. Расписание врача
    @GetMapping("/vets/{vetId}/schedule")
    public ResponseEntity<List<VetScheduleDto>> getVetSchedule(@PathVariable Long vetId) {
        return ResponseEntity.ok(clinicService.getVetSchedule(vetId));
    }

    // ✅ 5. Отчёт по активным приёмам
    @GetMapping("/reports/active-visits")
    public ResponseEntity<List<Map<String, Object>>> getActiveVisits() {
        return ResponseEntity.ok(clinicService.getActiveAppointments());
    }
}