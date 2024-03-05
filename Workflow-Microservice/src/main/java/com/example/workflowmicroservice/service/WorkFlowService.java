package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.dto.WFStepDTO;
import com.example.workflowmicroservice.dto.WorkFlowDTO;
import com.example.workflowmicroservice.exceptionhandling.ObjectNotFoundException;
import com.example.workflowmicroservice.model.EntityType;
import com.example.workflowmicroservice.model.WFStep;
import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.repository.EntityTypeRepository;
import com.example.workflowmicroservice.repository.WFRepository;
import com.example.workflowmicroservice.repository.WFStepRepository;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.workflowmicroservice.exceptionhandling.ErrorsEnum.*;

@Service
@RequiredArgsConstructor
public class WorkFlowService {

    private final WFRepository WFRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final WorkFlowMapper mapper;
    private final WFStepRepository wfStepRepository;
    @Transactional
    public ResponseEntity<WorkFlowDTO> createWorkFlowWithSteps(WorkFlowDTO workFlowDTO) {
        EntityType entityType = entityTypeRepository.findById(workFlowDTO.getEntityTypeId())
                .orElseThrow(()-> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));

        // Map the Work Flow DTO to Work Flow and Set the entity type base on the fetched type using its id
        WorkFlow workFlow = mapper.mapToEntity(workFlowDTO);
        workFlow.setEntityType(entityType);

        List<WFStep> wfSteps = new ArrayList<>();

        // Looping the WorkFlow DTO to get the Work Flow steps DTO and map it to Work Flow Steps
        for(WFStepDTO wfStepDTO : workFlowDTO.getWfSteps()){
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

        if (workFlows.isEmpty()){
            throw new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message);
        }

        List<WorkFlowDTO> workFlowDTOS = workFlows.stream().map(mapper::mapToDTO).toList();

        for (int i = 0; i < workFlowDTOS.size(); i++) {
            workFlowDTOS.get(i).setEntityTypeId(workFlows.get(i).getEntityType().getId());
        }

        return new ResponseEntity<>(workFlowDTOS, HttpStatus.OK);
    }

    public ResponseEntity<WorkFlowDTO> getWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));
        WorkFlowDTO workFlowDTO = mapper.mapToDTO(workFlow);
        workFlowDTO.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDTO, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<WorkFlowDTO> updateWorkFlowAttributesByWorkFlowId(
            Long id,
            String attributeName,
            Object requestBody) {

        WorkFlow workFlow = WFRepository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));

        switch (attributeName.toLowerCase()){
            case "version":{
                if (requestBody instanceof String) {
                    workFlow.setVersion((String) requestBody);
                    WFRepository.save(workFlow);
                }else
                    throw new RuntimeException(BAD_REQUEST.message);

                break;
            }
            case "description":{
                if (requestBody instanceof String){
                    workFlow.setDescription((String) requestBody);
                    WFRepository.save(workFlow);
                }else
                    throw new RuntimeException(BAD_REQUEST.message);

                break;
            }
            case "entitytypeid":{
                if (requestBody instanceof Integer) {
                    EntityType entityType = entityTypeRepository.findById(Long.valueOf((Integer) requestBody))
                            .orElseThrow(() -> new ObjectNotFoundException(ENTITY_TYPE_NOT_FOUND.message));
                    workFlow.setEntityType(entityType);
                    WFRepository.save(workFlow);
                }else
                    throw new RuntimeException(BAD_REQUEST.message);

                break;
            }
            default:{
                throw new RuntimeException(NO_SUCH_ATTRIBUTE.message);
            }
        }

        // Map the updated work flow to work flow DTO to return it as a response
        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);
        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }


    @Transactional
    public ResponseEntity<WorkFlowDTO> updateWorkFlowStepsByWorkFlowId(Long id, List<WFStepDTO> newStepsDTO){
        WorkFlow workFlow = WFRepository.findById(id)
                .orElseThrow(()-> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));

        List<WFStep> oldWfSteps = workFlow.getWfSteps();
        List<WFStep> newWfSteps = new ArrayList<>();

        // Convert the Work Flow Steps DTO to Work Flow Steps
        for (WFStepDTO wfStepDTO : newStepsDTO) {
            WFStep wfStep = mapper.mapToEntity(wfStepDTO);
            wfStep.setWorkFlow(workFlow);
            newWfSteps.add(wfStep);
        }

        // Iterate over the old WFSteps
        for (WFStep oldWfStep : oldWfSteps) {
            // Find the Work Flow Steps with ids that's already existing to update its values with new Work Flow Step
            Optional<WFStep> matchingNewWfStep = newWfSteps.stream()
                    .filter(newWfStep -> newWfStep.getId().equals(oldWfStep.getId()))
                    .findFirst();

            // If a matching new Work Flow Step is found, update the old WFStep
            matchingNewWfStep.ifPresent(wfStepRepository::save);
        }

        // Add new WFSteps that don't already exist
        for (WFStep newWfStep : newWfSteps) {
            // Checking that there is no new Work Flow Step with matching ids
            boolean isNewWfStep = oldWfSteps.stream()
                    .noneMatch(oldWfStep -> oldWfStep.getId().equals(newWfStep.getId()));

            if (isNewWfStep) {
                // Add the new WFStep to the WorkFlow's list of WFSteps
                workFlow.getWfSteps().add(newWfStep);
            }
        }

        // After all that update the Work Flow with the updated steps
        WFRepository.save(workFlow);


        // Map the updated work flow to work flow DTO to return it as a response
        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);
        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> deleteWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));
        WFRepository.delete(workFlow);

        return new ResponseEntity<>(
                "Work Flow : " + workFlow.getId() + " deleted successfully",
                HttpStatus.OK);
    }


}

