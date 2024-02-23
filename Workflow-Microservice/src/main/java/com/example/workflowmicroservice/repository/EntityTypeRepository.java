package com.example.workflowmicroservice.repository;

import com.example.workflowmicroservice.model.EntityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityTypeRepository extends JpaRepository<EntityType, Long> {
    Optional<EntityType> findEntityTypeByTypeName(String typeName);
}
