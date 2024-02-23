package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.dto.WorkFlowDTO;
import com.example.workflowmicroservice.exceptionhandling.ObjectNotFoundException;
import com.example.workflowmicroservice.model.EntityType;
import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.repository.EntityTypeRepository;
import com.example.workflowmicroservice.repository.WFRepository;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkFlowService {

    private final WFRepository WFRepository;
    private final EntityTypeRepository entityTypeRepository;
    private final WorkFlowMapper mapper;

    @Transactional
    public ResponseEntity<WorkFlowDTO> createWorkFlow(WorkFlowDTO workFlowDTO) {
        EntityType entityType = entityTypeRepository.findById(workFlowDTO.getEntityTypeId())
                .orElseThrow(()-> new ObjectNotFoundException("Entity Type"));

        WorkFlow workFlow = mapper.mapToEntity(workFlowDTO);
        workFlow.setEntityType(entityType);

        WFRepository.save(workFlow);

        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);

        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());


        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    public ResponseEntity<List<WorkFlowDTO>> getAllWorkFlows() {
        List<WorkFlow> workFlows = WFRepository.findAll();

        if (workFlows.isEmpty()){
            throw new ObjectNotFoundException("Work Flows");
        }

        List<WorkFlowDTO> workFlowDTOS = workFlows.stream().map(mapper::mapToDTO).toList();

        for (int i = 0; i < workFlowDTOS.size(); i++) {
            workFlowDTOS.get(i).setEntityTypeId(workFlows.get(i).getEntityType().getId());
        }

        return new ResponseEntity<>(workFlowDTOS, HttpStatus.OK);
    }

    public ResponseEntity<WorkFlowDTO> getWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Work FLow"));
        WorkFlowDTO workFlowDTO = mapper.mapToDTO(workFlow);
        workFlowDTO.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDTO, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<WorkFlowDTO> updateWorkFlowEntityTypeById(Long id, Long entityTypeId) {

        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Work FLow"));
        EntityType entityType = entityTypeRepository.findById(entityTypeId).orElseThrow(()-> new ObjectNotFoundException("Entity Type"));

        workFlow.setEntityType(entityType);

        WFRepository.save(workFlow);

        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);

        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<WorkFlowDTO> updateWorkFlowVersionById(Long id, String version) {

        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Work FLow"));

        workFlow.setVersion(version);

        WFRepository.save(workFlow);

        WorkFlowDTO workFlowDto = mapper.mapToDTO(workFlow);

        workFlowDto.setEntityTypeId(workFlow.getEntityType().getId());

        return new ResponseEntity<>(workFlowDto, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> deleteWorkFlowById(Long id) {
        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Work FLow"));
        WFRepository.delete(workFlow);

        return new ResponseEntity<>(
                "Work Flow : " + mapper.mapToDTO(workFlow).getId() + " deleted successfully",
                HttpStatus.OK);
    }

}

