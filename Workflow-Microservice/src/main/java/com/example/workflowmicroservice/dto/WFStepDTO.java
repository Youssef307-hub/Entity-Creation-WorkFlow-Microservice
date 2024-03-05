package com.example.workflowmicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WFStepDTO {

    private Long id;

    @NotNull(message = "Please enter a step number!")
    @Min(value = 1, message = "Please enter a valid step number!")
    private int stepNumber;

    @NotEmpty(message = "Please enter a step name!")
    private String stepName;

    @NotEmpty(message = "Please enter the role of person who can do the step!")
    private String roleName;
}
