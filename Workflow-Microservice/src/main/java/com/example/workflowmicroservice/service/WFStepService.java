package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.dto.WFStepDTO;
import com.example.workflowmicroservice.exceptionhandling.ObjectNotFoundException;
import com.example.workflowmicroservice.model.WorkFlow;
import com.example.workflowmicroservice.model.WFStep;
import com.example.workflowmicroservice.repository.WFRepository;
import com.example.workflowmicroservice.repository.WFStepRepository;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static com.example.workflowmicroservice.exceptionhandling.ErrorsEnum.*;

@Service
@RequiredArgsConstructor
public class WFStepService {

    private final WFStepRepository stepRepository;
    private final WFRepository WFRepository;
    private final WorkFlowMapper mapper;

    @Transactional
    public ResponseEntity<WFStepDTO> createWorkFlowStep(WFStepDTO WFStepDTO){

        WorkFlow workFlow = WFRepository.findById(WFStepDTO.getWorkFlowId())
                .orElseThrow(()-> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));
        WFStep step = mapper.mapToEntity(WFStepDTO);
        step.setWorkFlow(workFlow);

        stepRepository.save(step);

        WFStepDTO wfStepDTO = mapper.mapToDTO(step);

        wfStepDTO.setWorkFlowId(workFlow.getEntityType().getId());


        return new ResponseEntity<>(wfStepDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<List<WFStepDTO>> getAllWorkFlowStepsByWorkFlowId(Long id){
        WorkFlow workFlow = WFRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException(WORK_FLOW_NOT_FOUND.message));
        List<WFStep> wfSteps = stepRepository.findWorkFlowStepsByWorkFlow(workFlow);

        if(wfSteps.isEmpty()){
            throw new ObjectNotFoundException(STEP_NOT_FOUND.message);
        }

        List<WFStepDTO> WFStepDTOS = wfSteps.stream().map(mapper::mapToDTO).toList();

        for(int i = 0; i < WFStepDTOS.size() ; i++){
            WFStepDTOS.get(i).setWorkFlowId(wfSteps.get(i).getWorkFlow().getId());
        }

        return new ResponseEntity<>(WFStepDTOS, HttpStatus.OK);
    }

}
