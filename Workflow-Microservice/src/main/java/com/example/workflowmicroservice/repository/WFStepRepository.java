package com.example.workflowmicroservice.repository;

import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.model.WFStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WFStepRepository extends JpaRepository<WFStep, Long> {
    List<WFStep> findWorkFlowStepsByWorkFlow(WorkFlow workFlow);

    @Query("SELECT COUNT(w) FROM WorkFlowStep w WHERE w.workFlow = :workFlow")
    Long countWorkFlowStepsByWorkFlow(WorkFlow workFlow);

    Optional<WFStep> findWorkFlowStepByRoleName(String roleName);
}
