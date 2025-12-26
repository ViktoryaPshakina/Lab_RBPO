package com.example.vetclinic.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalHistoryDto {
    private Long appointmentId;
    private LocalDateTime dateTime;
    private String reason;
    private String complaints;
    private String vetName;
    private String status;
    private List<String> medications;
    private String prescription;

    public MedicalHistoryDto() {}

    public MedicalHistoryDto(
            Long appointmentId,
            LocalDateTime dateTime,
            String reason,
            String complaints,
            String vetName,
            String status,
            List<String> medications,
            String prescription) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
        this.reason = reason;
        this.complaints = complaints;
        this.vetName = vetName;
        this.status = status;
        this.medications = medications;
        this.prescription = prescription;
    }

    public Long getAppointmentId() { return appointmentId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getReason() { return reason; }
    public String getComplaints() { return complaints; }
    public String getVetName() { return vetName; }
    public String getStatus() { return status; }
    public List<String> getMedications() { return medications; }
    public String getPrescription() { return prescription; }
}