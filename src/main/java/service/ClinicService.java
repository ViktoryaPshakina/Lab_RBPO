package com.example.vetclinic.service;

import com.example.vetclinic.dto.*;
import com.example.vetclinic.entity.*;
import com.example.vetclinic.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final AppointmentRepository appointmentRepository;
    private final TreatmentRepository treatmentRepository;

    // 1. БИЗНЕС-ОПЕРАЦИЯ: Регистрация владельца с питомцем
    @Transactional
    public String registerOwnerWithPet(OwnerWithPetRequest request) {
        Owner owner = ownerRepository.findByEmail(request.getEmail())
                .orElseGet(() -> ownerRepository.save(Owner.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .phone(request.getPhone())
                        .email(request.getEmail())
                        .build()));

        Pet pet = Pet.builder()
                .name(request.getPetName())
                .species(request.getSpecies())
                .breed(request.getBreed())
                .age(request.getAge())
                .owner(owner)
                .build();
        petRepository.save(pet);

        return "Успешно зарегистрировано: Питомец " + pet.getName() + ", Владелец " + owner.getLastName();
    }

    // 2. БИЗНЕС-ОПЕРАЦИЯ: История болезни питомца
    public List<MedicalHistoryDto> getMedicalHistory(Long petId) {
        List<Appointment> appointments = appointmentRepository.findByPetId(petId);
        List<MedicalHistoryDto> history = new ArrayList<>();

        for (Appointment app : appointments) {
            Treatment tr = treatmentRepository.findByAppointmentId(app.getId()).orElse(null);
            history.add(MedicalHistoryDto.builder()
                    .dateTime(app.getDateTime())
                    .reason(app.getReason())
                    .status(app.getStatus())
                    .prescription(tr != null ? tr.getPrescription() : "Назначений нет")
                    .medications(tr != null ? tr.getMedications() : new ArrayList<>())
                    .build());
        }
        return history;
    }

    // 3. БИЗНЕС-ОПЕРАЦИЯ: Расписание врача
    public List<VetScheduleDto> getVetSchedule(Long vetId) {
        List<Appointment> appointments = appointmentRepository.findByVetId(vetId);
        List<VetScheduleDto> schedule = new ArrayList<>();

        for (Appointment app : appointments) {
            schedule.add(VetScheduleDto.builder()
                    .dateTime(app.getDateTime())
                    .petName(app.getPet() != null ? app.getPet().getName() : "Не указан")
                    .petSpecies(app.getPet() != null ? app.getPet().getSpecies() : "Не указан")
                    .reason(app.getReason())
                    .status(app.getStatus())
                    .build());
        }
        return schedule;
    }

    // 5. БИЗНЕС-ОПЕРАЦИЯ: Отчет по всем/активным приемам
    public List<Map<String, Object>> getActiveAppointments() {
        // Берем ВСЕ приемы, чтобы точно увидеть данные в Postman
        List<Appointment> allApps = appointmentRepository.findAll();
        List<Map<String, Object>> report = new ArrayList<>();

        for (Appointment app : allApps) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", app.getId());
            row.put("date", app.getDateTime());
            row.put("reason", app.getReason());
            row.put("status", app.getStatus());

            if (app.getPet() != null) {
                row.put("petName", app.getPet().getName());
                if (app.getPet().getOwner() != null) {
                    row.put("owner", app.getPet().getOwner().getLastName());
                }
            }
            if (app.getVet() != null) {
                row.put("vet", app.getVet().getLastName());
            }
            report.add(row);
        }
        return report;
    }

    // 4. БИЗНЕС-ОПЕРАЦИЯ: Каскадное удаление
    @Transactional
    public String deleteOwnerCascade(Long ownerId) {
        if (ownerRepository.existsById(ownerId)) {
            ownerRepository.deleteById(ownerId);
            return "Владелец с ID " + ownerId + " и его питомцы удалены";
        }
        return "Владелец не найден";
    }
}