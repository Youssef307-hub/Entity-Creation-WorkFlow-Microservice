package com.example.customermicroservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity(name = "Customer")
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(name = "customer_email_unique", columnNames = "email")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @SequenceGenerator(
            name = "customer_sequence",
            sequenceName = "customer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_sequence"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false,
            insertable = false,
            columnDefinition = "BIGINT"
    )
    private Long id;

    @Column(
            name = "first_name",
            columnDefinition = "VARCHAR(255)"
    )
    private String firstName;

    @Column(
            name = "last_name",
            columnDefinition = "VARCHAR(255)"
    )
    private String lastName;

    @Column(
            name = "user_name",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String userName;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String email;

    @Column(
            name = "age",
            columnDefinition = "INT"
    )
    private int age;


    @Column(
            name = "status",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String status;

    @Column(
            name = "creation_date",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime creationDate;

    @Column(
            name = "created_by",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String createdBy;

    @Column(
            name = "last_modification_date",
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime lastModificationDate;

    @Column(
            name = "version",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String version;

}
