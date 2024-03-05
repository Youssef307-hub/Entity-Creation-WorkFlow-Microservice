package com.example.workflowmicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "EntityType")
@Table(
        name = "entity_type",
        uniqueConstraints = @UniqueConstraint(name = "type_name_unique", columnNames = "type_name")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityType {

    @Id
    @SequenceGenerator(
            name = "entity_type_sequence",
            sequenceName = "entity_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "entity_type_sequence"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "type_name",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String typeName;
}
