package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.exceptionhandling.ObjectNotFoundException;
import com.example.workflowmicroservice.model.*;
import com.example.workflowmicroservice.repository.*;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.example.workflowmicroservice.exceptionhandling.ErrorsEnum.*;
import static com.example.workflowmicroservice.model.StatusEnum.Approved;
import static com.example.workflowmicroservice.model.StatusEnum.PENDING;

@Service
@RequiredArgsConstructor
public class WFLogicService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WFLogicService.class);
    private final WFRepository WFRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final WFStepRepository WFStepRepository;
    private final LogRepository logRepository;
    private final LogHistoryRepository logHistoryRepository;
    private final WorkFlowMapper mapper;

    @Transactional
    public String initiateWorkFlow(String entityType, Long entity_id) {

        // Getting the entity type object from the database using the entity type name
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType)
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));

        // Getting work flow associated with the entity type
        WorkFlow workFlow = WFRepository.findWorkFlowByEntityType(entityTypeFromDB)
                .orElseThrow(() -> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));

        // Getting number of steps from the work flow that belongs to the entity type
        long numberOfSteps = WFStepRepository.countWorkFlowStepsByWorkFlow(workFlow);

        // Getting the step eligible for the logged-in user based on his role
        WFStep wfStep = findWorkFlowForRole();
        LOGGER.info("\nThe Work Flow step is: {}", wfStep);

        // Getting the number of the step that the user is eligible for
        int eligibleStepNumber = wfStep.getStepNumber();

        WFLog wfLog = createWfLog(entity_id, wfStep, entityTypeFromDB, workFlow);

        String status = getStatusForEligibleStep(eligibleStepNumber, numberOfSteps);

        saveLogToLogHistoryDB(wfLog);
        logRepository.save(wfLog);

        return status;
    }

    public List<Long> getPendingEntitiesIds(String entityType) {
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType)
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));

        // Getting the step eligible for the logged-in user based on his role
        WFStep wfStep = findWorkFlowForRole();

        // Getting the number of the step that the user is eligible for
        int eligibleStepNumber = wfStep.getStepNumber();

        return logRepository.findPendingEntitiesIds(eligibleStepNumber, entityTypeFromDB.getId());
    }

    public List<Long> getApprovedEntitiesIds(String entityType) {
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType)
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));

        // Getting work flow associated with the entity type
        WorkFlow workFlow = WFRepository.findWorkFlowByEntityType(entityTypeFromDB)
                .orElseThrow(() -> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));

        long numberOfSteps = WFStepRepository.countWorkFlowStepsByWorkFlow(workFlow);

        LOGGER.info("\nThe Number of steps = {}", numberOfSteps);

        return logRepository.findApprovedEntitiesIds(numberOfSteps, entityTypeFromDB.getId());
    }

    @Transactional
    public String updateEntityStatusById(String entityType, Long entity_id) {
        EntityType entityTypeFromDB = entityTypeRepository.findEntityTypeByTypeName(entityType)
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));

        // Getting work flow associated with the entity type
        WorkFlow workFlow = WFRepository.findWorkFlowByEntityType(entityTypeFromDB)
                .orElseThrow(() -> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));

        // Getting current logged-in user role who created the entity
        WFStep wfStep = findWorkFlowForRole();

        // Getting the number of the step that the user is eligible for
        int eligibleStepNumber = wfStep.getStepNumber();

        // Getting the current step of the entity using the entity id
        Long currentStepNumber = logRepository.findStepNumberByEntityIdAndEntityType(entity_id, entityTypeFromDB.getId());

        // Getting number of steps from the work flow that belongs to the entity type
        long numberOfSteps = WFStepRepository.countWorkFlowStepsByWorkFlow(workFlow);

        // If current step number is less than the eligible step number then
        // Create New log with the new step number
        // Save the New log to log history table
        // Then Overwrite the old log with the new one to work flow log table
        if (currentStepNumber < eligibleStepNumber) {
            WFLog newWFLog = createWfLog(entity_id, wfStep, entityTypeFromDB, workFlow);

            saveLogToLogHistoryDB(newWFLog);
            WFLog oldLog = logRepository
                    .findWFLogByEntityIdAndEntityTypeId(entity_id, entityTypeFromDB.getId());
            newWFLog.setId(oldLog.getId());
            logRepository.save(newWFLog);

            return getStatusForEligibleStep(eligibleStepNumber, numberOfSteps);
        }
        return null;
    }

    private String getStatusForEligibleStep(int eligibleStepNumber, Long numberOfSteps) {
        // If the step number is less than or equal the total number of steps then return the status
        // and that will be the current step of the user
        String status = null;
        if (eligibleStepNumber < numberOfSteps)
            status = PENDING.value;
        else if (eligibleStepNumber == numberOfSteps) {
            status = Approved.value;
        }
        return status;
    }

    private WFLog createWfLog(Long entity_id, WFStep wfStep, EntityType entityTypeFromDB, WorkFlow workFlow) {
        // Create the work flow log that creation transaction
        return WFLog.builder()
                .step(wfStep)
                .createdBy(extractUserName())
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .entityTypeId(entityTypeFromDB.getId())
                .entityId(entity_id)
                .workFlow(workFlow)
                .build();
    }

    private void saveLogToLogHistoryDB(WFLog wfLog) {
        // Convert the log type to log history type, so it can be persisted in mongoDB
        WFLogHistory wfLogHistory = mapper.mapToHistoryFromLog(wfLog);
        wfLogHistory.setWorkFlowId(wfLog.getWorkFlow().getId());

        // Save the log history to the mongoDB
        logHistoryRepository.save(wfLogHistory);
    }

    private List<String> extractUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    private WFStep findWorkFlowForRole() {
        List<String> userRoles = extractUserRole();
        WFStep result = null;

        for (String userRole : userRoles) {
            Optional<WFStep> wfStep = WFStepRepository.findWorkFlowStepByRoleName(userRole);
            if (wfStep.isPresent())
                result = wfStep.get();
        }

        if (result == null) {
            throw new ObjectNotFoundException(STEP_NOT_FOUND.message);
        }

        return result;
    }

    private String extractUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        return jwt.getClaim("preferred_username");
    }
}
