package com.example.workflowmicroservice.repository;

import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.model.WFStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WFStepRepository extends JpaRepository<WFStep, Long> {
    List<WFStep> findWorkFlowStepsByWorkFlow(WorkFlow workFlow);

    long countWorkFlowStepsByWorkFlow(WorkFlow workFlow);

    WFStep findWorkFlowStepByRoleName(String roleName);
}
