package com.example.customermicroservice.dto;

import com.example.customermicroservice.validation.FirstOrLastNameNotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FirstOrLastNameNotNull(
        firstName = "firstName",
        lastName = "lastName"
)
public class CustomerRequestDTO {

    String firstName;
    String lastName;
    @NotEmpty(message = "Please enter your username!")
    String userName;
    @NotEmpty(message = "Please enter your email!")
    @Email(message = "Please enter a valid email!")
    String email;
    @Min(value = 8, message = "Please enter a valid age!")
    @Max(value = 100, message = "Please enter a valid age!")
    int age;
    @NotEmpty(message = "Version can't be null!")
    String version;
    @JsonIgnore
    private Long id;


}
