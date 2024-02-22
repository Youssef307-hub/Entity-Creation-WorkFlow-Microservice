package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.dto.WorkFlowDTO;
import com.example.workflowmicroservice.model.EntityType;
import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.repository.EntityTypeRepository;
import com.example.workflowmicroservice.repository.LogRepository;
import com.example.workflowmicroservice.repository.WFRepository;
import com.example.workflowmicroservice.repository.WFStepRepository;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkFlowService {

    private final WFRepository WFRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final WorkFlowMapper mapper;

    public ResponseEntity<WorkFlowDTO> createWorkFlow(WorkFlowDTO workFlowDTO) {
        EntityType entityType = entityTypeRepository.findById(workFlowDTO.getEntityTypeId()).get();

        WorkFlow workFlow = mapper.mapToEntity(workFlowDTO);
        workFlow.setEntityType(entityType);

        WFRepository.save(workFlow);

        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);

        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());


        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    public ResponseEntity<List<WorkFlowDTO>> getAllWorkFlows() {
        List<WorkFlow> workFlows = WFRepository.findAll();

        List<WorkFlowDTO> workFlowDTOS = workFlows.stream().map(mapper::mapToDTO).toList();

        for (int i = 0; i < workFlowDTOS.size(); i++) {
            workFlowDTOS.get(i).setEntityTypeId(workFlows.get(i).getEntityType().getId());
        }

        return new ResponseEntity<>(workFlowDTOS, HttpStatus.OK);
    }

    public ResponseEntity<WorkFlowDTO> getWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).get();
        WorkFlowDTO workFlowDTO = mapper.mapToDTO(workFlow);
        workFlowDTO.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDTO, HttpStatus.OK);
    }

    public ResponseEntity<WorkFlowDTO> updateWorkFlowEntityTypeById(Long id, Long entityTypeId) {

        WorkFlow workFlow = WFRepository.findById(id).get();
        EntityType entityType = entityTypeRepository.findById(entityTypeId).get();

        workFlow.setEntityType(entityType);

        WFRepository.save(workFlow);

        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);

        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    public ResponseEntity<WorkFlowDTO> updateWorkFlowVersionById(Long id, String version) {

        WorkFlow workFlow = WFRepository.findById(id).get();

        workFlow.setVersion(version);

        WFRepository.save(workFlow);

        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);

        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).get();
        WFRepository.delete(workFlow);

        return new ResponseEntity<>(
                "Work Flow : " + mapper.mapToDTO(workFlow).getId() + " deleted successfully",
                HttpStatus.OK);
    }

}

