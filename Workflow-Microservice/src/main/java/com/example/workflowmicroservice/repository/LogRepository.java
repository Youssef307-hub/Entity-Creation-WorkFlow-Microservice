package com.example.workflowmicroservice.repository;

import com.example.workflowmicroservice.model.WFLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<WFLog, Long> {
    @Query("SELECT log.entityId FROM WorkFlowLog log WHERE log.step.id < :numberOfSteps AND log.entityTypeId = :entityTypeId")
    List<Long> findPendingEntitiesIds(Long numberOfSteps, Long entityTypeId);

    @Query("SELECT log.entityId FROM WorkFlowLog log WHERE log.step.id = :numberOfSteps AND log.entityTypeId = :entityTypeId")
    List<Long> findApprovedEntitiesIds(Long numberOfSteps, Long entityTypeId);

    @Query("SELECT log.step.id FROM WorkFlowLog log WHERE log.entityId = :entityId AND log.entityTypeId = :entityTypeId")
    Long findStatusByEntityIdAndEntityType(Long entityId, Long entityTypeId);

    WFLog findWFLogByEntityIdAndEntityTypeId(Long entityId, Long entityTypeId);
}
