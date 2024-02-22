package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.model.*;
import com.example.workflowmicroservice.repository.*;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WFLogicService {

    private final WFRepository WFRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final WFStepRepository WFStepRepository;
    private final LogRepository logRepository;
    private final LogHistoryRepository logHistoryRepository;
    private final WorkFlowMapper mapper;

    public String initiateWorkFlow(String entityType, Long entity_id){

        // Getting the entity type object from the database using the entity type name
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType);

        // Getting work flow associated with the entity type
        WorkFlow workFlow = WFRepository.findWorkFlowByEntityType(entityTypeFromDB);

        // Getting number of steps from the work flow that belongs to the entity type
        long numberOfSteps = WFStepRepository.countWorkFlowStepsByWorkFlow(workFlow);

        // Getting current logged-in user role who created the entity
        String userRole = extractUserRole();

        // Getting the step eligible for the logged-in user based on his role
        WFStep wfStep = WFStepRepository.findWorkFlowStepByRoleName(userRole);

        // Getting the number of the step that the user is eligible for
        int eligibleStepNumber = wfStep.getStepNumber();

        // Save in the work flow log that creation transaction
        WFLog wfLog = WFLog.builder()
                .createdBy(extractUserName())
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .step(wfStep)
                .entityTypeId(entityTypeFromDB.getId())
                .entityId(entity_id)
                .workFlow(workFlow)
                .build();

        // if the step number is less than or equal the total number of steps then return the step name
        // and that will be the current step of the user
        if(eligibleStepNumber <= numberOfSteps){
            logRepository.save(wfLog);
            return wfStep.getStepName();
        }

        return null;
    }

    public List<Long> getPendingEntitiesIds(String entityType){
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType);

        long numberOfSteps = getStepNumberOfWF(entityTypeFromDB);

        return logRepository.findPendingEntitiesIds(numberOfSteps, entityTypeFromDB.getId());
    }

    public List<Long> getApprovedEntitiesIds(String entityType){
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType);

        long numberOfSteps = getStepNumberOfWF(entityTypeFromDB);

        return logRepository.findApprovedEntitiesIds(numberOfSteps, entityTypeFromDB.getId());
    }

    public String updateEntityStatusById(String entityType, Long entity_id){
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType);

        // Getting work flow associated with the entity type
        WorkFlow workFlow = WFRepository.findWorkFlowByEntityType(entityTypeFromDB);

        // Getting current logged-in user role who created the entity
        String userRole = extractUserRole();

        // Getting the step eligible for the logged-in user based on his role
        WFStep wfStep = WFStepRepository.findWorkFlowStepByRoleName(userRole);

        // Getting the number of the step that the user is eligible for
        int eligibleStepNumber = wfStep.getStepNumber();

        // Getting the current step of the entity using the entity id
        Long currentStepNumber = logRepository.findStatusByEntityIdAndEntityType(entity_id, entityTypeFromDB.getId());

        // If current step number is less than the eligible step number then
        // drop the log to the log history
        // create new log with the new step number
        // save the log to logDB
        if(currentStepNumber < eligibleStepNumber){
            dropLogToLogHistoryDB(logRepository.findWFLogByEntityIdAndEntityTypeId(entity_id, entityTypeFromDB.getId()).getId());
            WFLog wfLog = WFLog.builder()
                    .createdBy(extractUserName())
                    .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .step(wfStep)
                    .entityTypeId(entityTypeFromDB.getId())
                    .entityId(entity_id)
                    .workFlow(workFlow)
                    .build();

            logRepository.save(wfLog);
            return wfStep.getStepName();
        }
        return null;
    }

    private void dropLogToLogHistoryDB(Long logId){

        // First find the log in the WFLog DB
        WFLog wfLog = logRepository.findById(logId).get();

        // Convert the log type to log history type, so it can be persisted in mongoDB
        WFLogHistory wfLogHistory = mapper.mapToHistoryFromLog(wfLog);
        wfLogHistory.setWorkFlowId(wfLog.getWorkFlow().getId());
        wfLogHistory.setStepId(wfLog.getStep().getId());

        // Saving the log history to the mongoDB
        logHistoryRepository.save(wfLogHistory);

        // Deleting the log from WFLog DB
        logRepository.deleteById(logId);
    }

    private Long getStepNumberOfWF(EntityType entityType){
        // Getting work flow associated with the entity type
        WorkFlow workFlow = WFRepository.findWorkFlowByEntityType(entityType);

        // Getting number of steps from the work flow that belongs to the entity type
        return WFStepRepository.countWorkFlowStepsByWorkFlow(workFlow);
    }

    private String extractUserRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if(authorities.contains("ROLE_ADMIN")){
            return "ROLE_ADMIN";
        } else if (authorities.contains("ROLE_MANAGER")) {
            return "ROLE_MANAGER";
        }else
            return "ROLE_USER";
    }

    private String extractUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        return jwt.getClaim("preferred_username");
    }
}
