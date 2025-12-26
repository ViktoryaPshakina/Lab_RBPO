package com.example.vetclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OwnerWithPetRequest {

    @NotBlank(message = "Имя владельца не может быть пустым")
    private String firstName;

    @NotBlank(message = "Фамилия владельца не может быть пустой")
    private String lastName;

    @NotBlank(message = "Телефон владельца обязателен")
    private String phone;

    @NotBlank(message = "Email владельца обязателен")
    private String email;

    @NotBlank(message = "Имя питомца не может быть пустым")
    private String petName;

    @NotBlank(message = "Вид питомца не может быть пустым")
    private String species;

    @NotNull(message = "Возраст питомца обязателен")
    @Positive(message = "Возраст должен быть положительным числом")
    private Integer age;

    // Геттеры и сеттеры
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}