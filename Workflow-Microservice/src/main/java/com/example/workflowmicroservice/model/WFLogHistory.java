package com.example.workflowmicroservice.model;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "work_flow_log_history")
public class WFLogHistory {

    @Id
    private Long id;

    private String createdBy;

    private LocalDateTime creationDate;

    private Long stepNumber;

    private Long entityTypeId;

    private Long entityId;

    private Long workFlowId;

}
