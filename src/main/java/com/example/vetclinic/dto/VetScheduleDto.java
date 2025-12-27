package com.example.vetclinic.dto;

import java.time.LocalDateTime;

public class VetScheduleDto {
    private Long appointmentId;
    private LocalDateTime dateTime;
    private String ownerName;
    private String petName;
    private String complaints;
    private String status;

    public VetScheduleDto() {}

    public VetScheduleDto(
            Long appointmentId,
            LocalDateTime dateTime,
            String ownerName,
            String petName,
            String complaints,
            String status) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
        this.ownerName = ownerName;
        this.petName = petName;
        this.complaints = complaints;
        this.status = status;
    }

    public Long getAppointmentId() { return appointmentId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getOwnerName() { return ownerName; }
    public String getPetName() { return petName; }
    public String getComplaints() { return complaints; }
    public String getStatus() { return status; }
}