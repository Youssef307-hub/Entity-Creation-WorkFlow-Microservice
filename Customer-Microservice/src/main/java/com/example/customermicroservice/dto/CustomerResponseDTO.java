package com.example.customermicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {

    private Long id;

    String firstName;

    String lastName;

    String userName;

    String email;

    int age;

    String status;

    LocalDateTime creationDate;

    String createdBy;

    LocalDateTime lastModificationDate;

    String version;
}
