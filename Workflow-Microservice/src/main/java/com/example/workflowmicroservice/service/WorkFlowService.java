package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.dto.WFStepDTO;
import com.example.workflowmicroservice.dto.WorkFlowDTO;
import com.example.workflowmicroservice.exceptionhandling.ObjectNotFoundException;
import com.example.workflowmicroservice.model.EntityType;
import com.example.workflowmicroservice.model.WFStep;
import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.repository.EntityTypeRepository;
import com.example.workflowmicroservice.repository.WFRepository;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.workflowmicroservice.exceptionhandling.ErrorsEnum.ENTITY_TYPE_NOT_FOUND;
import static com.example.workflowmicroservice.exceptionhandling.ErrorsEnum.WORK_FLOW_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WorkFlowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowService.class);
    private final WFRepository WFRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final WorkFlowMapper mapper;

    @Transactional
    public ResponseEntity<WorkFlowDTO> createWorkFlowWithSteps(WorkFlowDTO workFlowDTO) {
        EntityType entityType = entityTypeRepository.findById(workFlowDTO.getEntityTypeId())
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));

        // Map the Work Flow DTO to Work Flow and Set the entity type base on the fetched type using its id
        WorkFlow workFlow = mapper.mapToEntity(workFlowDTO);
        workFlow.setEntityType(entityType);

        List<WFStep> wfSteps = new ArrayList<>();

        // Looping the WorkFlow DTO to get the Work Flow steps DTO and map it to Work Flow Steps
        for (WFStepDTO wfStepDTO : workFlowDTO.getWfSteps()) {
            WFStep wfStep = mapper.mapToEntity(wfStepDTO);
            wfStep.setWorkFlow(workFlow);
            wfSteps.add(wfStep);
        }

        // Adding to the Work Flow the fetched Work Flow Steps
        workFlow.setWfSteps(wfSteps);

        // When Saving the Work Flow automatically the Work Flow Steps Will be saved,
        // as the cascade type is for all operations
        WFRepository.save(workFlow);

        // Creating DTO to be returned
        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);
        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    public ResponseEntity<List<WorkFlowDTO>> getAllWorkFlows() {
        List<WorkFlow> workFlows = WFRepository.findAll();

        if (workFlows.isEmpty()) {
            throw new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message);
        }

        List<WorkFlowDTO> workFlowDTOS = workFlows.stream().map(mapper::mapToDTO).toList();

        for (int i = 0; i < workFlowDTOS.size(); i++) {
            workFlowDTOS.get(i).setEntityTypeId(workFlows.get(i).getEntityType().getId());
        }
        LOGGER.info("The work flows are {}", workFlows);

        return new ResponseEntity<>(workFlowDTOS, HttpStatus.OK);
    }

    public ResponseEntity<WorkFlowDTO> getWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));
        WorkFlowDTO workFlowDTO = mapper.mapToDTO(workFlow);
        workFlowDTO.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDTO, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<WorkFlowDTO> updateWorkFlow(WorkFlowDTO newWorkFlowDTO) {
        WorkFlow oldWorkFlow = WFRepository.findById(newWorkFlowDTO.getId())
                .orElseThrow(() -> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));

        EntityType entityType = entityTypeRepository.findById(newWorkFlowDTO.getEntityTypeId())
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));

        WorkFlow newWorkFlow = mapper.mapToEntity(newWorkFlowDTO);

        newWorkFlow.setId(oldWorkFlow.getId());
        newWorkFlow.setEntityType(entityType);
        newWorkFlow.getWfSteps().forEach(wfStep -> wfStep.setWorkFlow(newWorkFlow));

        WFRepository.save(newWorkFlow);

        WorkFlowDTO result = mapper.mapToDTO(newWorkFlow);
        result.setEntityTypeId(entityType.getId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));
        WFRepository.delete(workFlow);

        return new ResponseEntity<>(
                "Work Flow : " + workFlow.getId() + " deleted successfully",
                HttpStatus.OK);
    }


}

