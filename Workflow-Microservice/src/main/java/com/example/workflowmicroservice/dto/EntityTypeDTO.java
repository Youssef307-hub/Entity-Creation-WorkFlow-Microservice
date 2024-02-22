package com.example.workflowmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityTypeDTO {

    @JsonIgnore
    private Long id;

    @NotEmpty(message = "Please enter an entity type name!")
    private String typeName;
}
