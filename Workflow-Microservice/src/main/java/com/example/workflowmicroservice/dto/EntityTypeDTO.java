package com.example.workflowmicroservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityTypeDTO {

    private Long id;

    @NotEmpty(message = "Please enter an entity type name!")
    private String typeName;
}
