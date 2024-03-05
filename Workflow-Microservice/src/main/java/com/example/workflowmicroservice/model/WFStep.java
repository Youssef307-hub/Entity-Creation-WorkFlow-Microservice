package com.example.workflowmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "WorkFlowStep")
@Table(
        name = "work_flow_step",
        uniqueConstraints = {
                @UniqueConstraint(name = "step_number_within_workflow_unique", columnNames = {"step_number", "work_flow_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WFStep {

    @Id
    @SequenceGenerator(
            name = "work_flow_step_sequence",
            sequenceName = "work_flow_step_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "work_flow_step_sequence"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "work_flow_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "work_flow_id_fk"
            )
    )
    private WorkFlow workFlow;

    @Column(
            name = "step_number",
            nullable = false,
            columnDefinition = "INT"
    )
    private int stepNumber;

    @Column(
            name = "step_name",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String stepName;

    @Column(
            name = "role_name",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String roleName;
}
