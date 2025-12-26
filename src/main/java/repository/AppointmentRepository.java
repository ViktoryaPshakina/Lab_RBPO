package com.example.vetclinic.repository;

import com.example.vetclinic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPetIdOrderByDateTimeDesc(Long petId);
    List<Appointment> findByVetIdOrderByDateTimeAsc(Long vetId);

    // ✅ ДОБАВЬТЕ ЭТОТ МЕТОД
    List<Appointment> findByStatus(String status);
}