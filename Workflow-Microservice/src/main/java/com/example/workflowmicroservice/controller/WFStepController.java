package com.example.workflowmicroservice.controller;

import com.example.workflowmicroservice.dto.WFStepDTO;
import com.example.workflowmicroservice.service.WFStepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/work-flow-step")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Work Flow Step Creation", description = "Work Flow Step Management APIs")
public class WFStepController {

    private final WFStepService WFStepService;

    @Operation(
            summary = "Create New Work Flow Step",
            description = "Create new work flow step," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "POST")
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WFStepDTO> createWorkflowStep(@Valid @RequestBody WFStepDTO WFStepDTO){
        return WFStepService.createWorkFlowStep(WFStepDTO);
    };

    @Operation(
            summary = "Get All Steps",
            description = "Get all steps associated with one work flow," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "GET")
    @GetMapping("/{workFlowId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WFStepDTO>> getAllWorkFlowStepsByWorkFlowId(@PathVariable Long workFlowId){
        return WFStepService.getAllWorkFlowStepsByWorkFlowId(workFlowId);
    };

}
