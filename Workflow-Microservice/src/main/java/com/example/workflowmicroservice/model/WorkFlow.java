package com.example.workflowmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "WorkFlow")
@Table(
        name = "work_flow",
        uniqueConstraints = @UniqueConstraint(name = "entity_type_id_unique", columnNames = "entity_type_id")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkFlow {

    @Id
    @SequenceGenerator(
            name = "work_flow_sequence",
            sequenceName = "work_flow_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "work_flow_sequence"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "entity_type_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "entity_type_id_fk"
            )
    )
    private EntityType entityType;

    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "version",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String version;

    @OneToMany(
            mappedBy = "workFlow",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WFStep> wfSteps;
}
