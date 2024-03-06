package com.example.workflowmicroservice.controller;

import com.example.workflowmicroservice.dto.WorkFlowDTO;
import com.example.workflowmicroservice.service.WorkFlowService;
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
@RequestMapping("/work-flow")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Work Flow Creation", description = "Work Flow Management APIs")
public class WFController {

    private final WorkFlowService workFlowService;

    @Operation(
            summary = "Create New Work Flow Along With Its Steps",
            description = "Create a new work flow," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "POST")
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkFlowDTO> createWorkflow(@Valid @RequestBody WorkFlowDTO workFlowDTO) {
        return workFlowService.createWorkFlowWithSteps(workFlowDTO);
    }

    @Operation(
            summary = "Get All Work Flows ",
            description = "Get all work flows," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "GET")
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WorkFlowDTO>> getAllWorkFlows() {
        return workFlowService.getAllWorkFlows();
    }

    @Operation(
            summary = "Get Work Flow By Id",
            description = "Get work flow by it's id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "GET")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkFlowDTO> getWorkFlowById(@PathVariable Long id) {
        return workFlowService.getWorkFlowById(id);
    }

    @Operation(
            summary = "Update Work Flow Along With Its Steps",
            description = "Update work flow attributes by passing the work flow dto in the request body," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "PUT")
    @PutMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkFlowDTO> updateWorkFlow(
            @RequestBody WorkFlowDTO workFlowDTO) {
        return workFlowService.updateWorkFlow(workFlowDTO);
    }

    @Operation(
            summary = "Delete Work Flow By Id",
            description = "Delete work flow by it's id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "DELETE")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteWorkFlowById(@PathVariable Long id) {
        return workFlowService.deleteWorkFlowById(id);
    }


}
