package com.example.workflowmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WFLogDTO {

    private Long id;

    private String createdBy;

    private LocalDateTime creationDate;

    private Long stepNumber;

    private Long entityTypeId;

    private Long entityId;

    private Long workFlowId;
}
