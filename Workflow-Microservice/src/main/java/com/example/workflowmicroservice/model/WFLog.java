package com.example.workflowmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "WorkFlowLog")
@Table(name = "work_flow_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WFLog {

    @Id
    @SequenceGenerator(
            name = "work_flow_log_sequence",
            sequenceName = "work_flow_log_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "work_flow_log_sequence"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "created_by",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String createdBy;

    @Column(
            name = "creation_date",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(
            name = "step_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "step_id_fk"
            )
    )
    private WFStep step;

    @Column(
            name = "entity_type_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long entityTypeId;

    @Column(
            name = "entity_id",
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long entityId;

    @ManyToOne
    @JoinColumn(
            name = "work_flow_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "work_flow_id_fk"
            )

    )
    private WorkFlow workFlow;
}
