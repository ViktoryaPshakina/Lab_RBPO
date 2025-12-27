package com.example.vetclinic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerWithPetRequest {
    @JsonProperty("ownerFirstName")
    private String firstName;
    @JsonProperty("ownerLastName")
    private String lastName;
    @JsonProperty("ownerPhone")
    private String phone;
    @JsonProperty("ownerEmail")
    private String email;

    private String petName;
    private String species;
    private String breed;
    private Integer age;
}