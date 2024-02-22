package com.example.workflowmicroservice.utility;

import com.example.workflowmicroservice.dto.EntityTypeDTO;
import com.example.workflowmicroservice.dto.WorkFlowDTO;
import com.example.workflowmicroservice.dto.WFLogDTO;
import com.example.workflowmicroservice.dto.WFStepDTO;
import com.example.workflowmicroservice.model.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkFlowMapper {

    WorkFlowDTO mapToDTO(WorkFlow workFlow);
    WorkFlow mapToEntity(WorkFlowDTO workFlowDTO);

    EntityTypeDTO mapToDTO(EntityType entityType);
    EntityType mapToEntity(EntityTypeDTO entityTypeDTO);

    WFLogDTO mapToDTO(WFLog WFLog);
    WFLog mapToEntity(WFLogDTO WFLogDTO);

    WFStepDTO mapToDTO(WFStep WFStep);
    WFStep mapToEntity(WFStepDTO WFStepDTO);

    WFLogHistory mapToHistoryFromLog(WFLog WFLog);

    WFLogDTO mapHistoryToDTO(WFLogHistory wfLogHistory);


}
