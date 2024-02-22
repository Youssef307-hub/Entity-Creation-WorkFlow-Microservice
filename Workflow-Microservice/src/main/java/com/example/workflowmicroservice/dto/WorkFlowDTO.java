package com.example.workflowmicroservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowDTO {

    @JsonIgnore
    private Long id;

    @NotEmpty(message = "Please enter the entity type id!")
    @Min(value = 1 , message = "Please enter a valid entity type id!")
    private Long entityTypeId;

    private String description;

    @NotEmpty(message = "Please enter the workflow version!")
    private String version;

}
