package com.example.vetclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistoryDto {
    private LocalDateTime dateTime; // Дата приема
    private String reason;          // Причина обращения
    private String status;          // Статус (завершен/активен)
    private String prescription;    // Предписания врача
    private List<String> medications; // Список лекарств
}