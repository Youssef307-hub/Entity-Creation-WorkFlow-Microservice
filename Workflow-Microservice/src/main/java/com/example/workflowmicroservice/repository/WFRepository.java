package com.example.workflowmicroservice.repository;

import com.example.workflowmicroservice.model.EntityType;
import com.example.workflowmicroservice.model.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WFRepository extends JpaRepository<WorkFlow, Long> {
    WorkFlow findWorkFlowByEntityType(EntityType entityType);
}
