package com.example.vetclinic.service;

import com.example.vetclinic.dto.MedicalHistoryDto;
import com.example.vetclinic.dto.VetScheduleDto;
import com.example.vetclinic.entity.*;
import com.example.vetclinic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClinicService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private VetRepository vetRepository;

    // 1. Регистрация владельца с питомцем
    @Transactional
    public Map<String, Object> registerOwnerWithPet(String firstName, String lastName, String phone, String email, String petName, String species, Integer age) {
        Owner owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setPhone(phone);
        owner.setEmail(email);
        owner = ownerRepository.save(owner);

        Pet pet = new Pet();
        pet.setName(petName);
        pet.setSpecies(species);
        pet.setAge(age);
        pet.setOwner(owner);
        pet = petRepository.save(pet);

        Map<String, Object> result = new HashMap<>();
        result.put("ownerId", owner.getId());
        result.put("petId", pet.getId());
        result.put("message", "Владелец и питомец успешно зарегистрированы");
        return result;
    }

    // 2. История болезни питомца
    public List<MedicalHistoryDto> getMedicalHistory(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Питомец не найден"));

        return appointmentRepository.findByPetIdOrderByDateTimeDesc(petId).stream()
                .map(appointment -> {
                    String vetName = appointment.getVet().getFirstName() + " " + appointment.getVet().getLastName();
                    Treatment treatment = treatmentRepository.findByAppointmentId(appointment.getId());

                    List<String> medications = (treatment != null && treatment.getMedications() != null)
                            ? treatment.getMedications()
                            : List.of();
                    String prescription = (treatment != null)
                            ? treatment.getPrescription()
                            : "Лечение не назначено";

                    return new MedicalHistoryDto(
                            appointment.getId(),
                            appointment.getDateTime(),
                            appointment.getReason(),
                            appointment.getComplaints(),
                            vetName,
                            appointment.getStatus(),
                            medications,
                            prescription
                    );
                })
                .collect(Collectors.toList());
    }

    // 3. Расписание врача
    public List<VetScheduleDto> getVetSchedule(Long vetId) {
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new RuntimeException("Врач не найден"));

        return appointmentRepository.findByVetIdOrderByDateTimeAsc(vetId).stream()
                .map(appointment -> {
                    String ownerName = appointment.getPet().getOwner().getFirstName() + " " + appointment.getPet().getOwner().getLastName();
                    String petName = appointment.getPet().getName();
                    String complaints = appointment.getComplaints() != null ? appointment.getComplaints() : "Жалобы не указаны";

                    return new VetScheduleDto(
                            appointment.getId(),
                            appointment.getDateTime(),
                            ownerName,
                            petName,
                            complaints,
                            appointment.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    // 4. Удаление владельца с каскадом
    @Transactional
    public String deleteOwnerCascade(Long ownerId) {
        if (!ownerRepository.existsById(ownerId)) {
            throw new RuntimeException("Владелец не найден");
        }
        ownerRepository.deleteById(ownerId);
        return "Владелец и все связанные данные успешно удалены";
    }

    // 5. Отчёт по активным приёмам
    public List<Map<String, Object>> getActiveAppointments() {
        return appointmentRepository.findByStatus("SCHEDULED").stream()
                .map(appointment -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", appointment.getId());
                    map.put("dateTime", appointment.getDateTime());
                    map.put("owner", appointment.getPet().getOwner().getFirstName() + " " + appointment.getPet().getOwner().getLastName());
                    map.put("pet", appointment.getPet().getName());
                    map.put("vet", appointment.getVet().getFirstName() + " " + appointment.getVet().getLastName());
                    map.put("reason", appointment.getReason());
                    map.put("status", appointment.getStatus());
                    return map;
                })
                .collect(Collectors.toList());
    }
}