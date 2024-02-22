package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.dto.WFStepDTO;
import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.model.WFStep;
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
public class WFStepService {

    private final WFStepRepository stepRepository;
    private final WFRepository WFRepository;
    private final WorkFlowMapper mapper;

    public ResponseEntity<WFStepDTO> createWorkFlowStep(WFStepDTO WFStepDTO){

        WorkFlow workFlow = WFRepository.findById(WFStepDTO.getWorkFlowId()).get();
        WFStep step = mapper.mapToEntity(WFStepDTO);
        step.setWorkFlow(workFlow);

        stepRepository.save(step);

        WFStepDTO wfStepDTO = mapper.mapToDTO(step);

        wfStepDTO.setWorkFlowId(workFlow.getEntityType().getId());


        return new ResponseEntity<>(wfStepDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<List<WFStepDTO>> getAllWorkFlowStepsByWorkFlowId(Long id){
        WorkFlow workFlow = WFRepository.findById(id).get();
        List<WFStep> wfSteps = stepRepository.findWorkFlowStepsByWorkFlow(workFlow);

        List<WFStepDTO> WFStepDTOS = wfSteps.stream().map(mapper::mapToDTO).toList();

        for(int i = 0; i < WFStepDTOS.size() ; i++){
            WFStepDTOS.get(i).setWorkFlowId(wfSteps.get(i).getWorkFlow().getId());
        }

        return new ResponseEntity<>(WFStepDTOS, HttpStatus.OK);
    }

}
