package com.example.vetclinic.dto;

public class OwnerWithPetResponse {
    private Long ownerId;
    private Long petId;
    private String ownerName;
    private String petName;
    private String message;

    public OwnerWithPetResponse(Long ownerId, Long petId, String ownerName, String petName) {
        this.ownerId = ownerId;
        this.petId = petId;
        this.ownerName = ownerName;
        this.petName = petName;
        this.message = "Владелец и питомец успешно зарегистрированы";
    }

    // Геттеры
    public Long getOwnerId() { return ownerId; }
    public Long getPetId() { return petId; }
    public String getOwnerName() { return ownerName; }
    public String getPetName() { return petName; }
    public String getMessage() { return message; }
}