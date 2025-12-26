package com.example.vetclinic.dto;

import com.example.vetclinic.entity.Appointment;

public class ActiveVisitDto {
    private Long appointmentId;
    private String ownerName;
    private String petName;
    private String vetName;
    private String reason;
    private String status;

    public ActiveVisitDto(Appointment appointment) {
        this.appointmentId = appointment.getId();
        this.ownerName = appointment.getPet().getOwner().getFirstName() + " " + appointment.getPet().getOwner().getLastName();
        this.petName = appointment.getPet().getName();
        this.vetName = appointment.getVet().getFirstName() + " " + appointment.getVet().getLastName();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
    }

    // Геттеры
    public Long getAppointmentId() { return appointmentId; }
    public String getOwnerName() { return ownerName; }
    public String getPetName() { return petName; }
    public String getVetName() { return vetName; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
}