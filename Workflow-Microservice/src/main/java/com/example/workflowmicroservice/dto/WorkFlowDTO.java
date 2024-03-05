package com.example.workflowmicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowDTO {

    private Long id;

    @NotNull(message = "Please enter the entity type id!")
    @Min(value = 1, message = "Please enter a valid entity type id!")
    private Long entityTypeId;

    private String description;

    @NotEmpty(message = "Please enter the workflow version!")
    private String version;

    @NotNull(message = "Please add work flow steps!")
    private List<WFStepDTO> wfSteps;

}
