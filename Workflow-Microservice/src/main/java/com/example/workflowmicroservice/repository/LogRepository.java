package com.example.workflowmicroservice.repository;

import com.example.workflowmicroservice.model.WFLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<WFLog, Long> {
    @Query("SELECT log.entityId " +
            "FROM WorkFlowLog log " +
            "WHERE (log.step.stepNumber < :eligibleStep)" +
            "AND log.entityTypeId = :entityTypeId")
    List<Long> findPendingEntitiesIds(int eligibleStep, Long entityTypeId);

    @Query("SELECT log.entityId FROM WorkFlowLog log WHERE log.step.stepNumber = :numberOfSteps AND log.entityTypeId = :entityTypeId")
    List<Long> findApprovedEntitiesIds(Long numberOfSteps, Long entityTypeId);

    @Query("SELECT log.step.stepNumber FROM WorkFlowLog log WHERE log.entityId = :entityId AND log.entityTypeId = :entityTypeId")
    Long findStepNumberByEntityIdAndEntityType(Long entityId, Long entityTypeId);

    WFLog findWFLogByEntityIdAndEntityTypeId(Long entityId, Long entityTypeId);
}
