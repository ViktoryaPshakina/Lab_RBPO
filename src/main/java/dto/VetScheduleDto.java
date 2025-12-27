package com.example.vetclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VetScheduleDto {
    private LocalDateTime dateTime; // Дата и время приема
    private String petName;          // Имя питомца
    private String petSpecies;       // Вид питомца (кот/собака)
    private String reason;           // Причина визита
    private String status;           // Статус (запланирован/завершен)
}