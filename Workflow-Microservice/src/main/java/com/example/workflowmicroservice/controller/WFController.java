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
            summary = "Create New Work Flow",
            description = "Create new work flow," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "POST")
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkFlowDTO> createWorkflow(@Valid @RequestBody WorkFlowDTO workFlowDTO){
        return workFlowService.createWorkFlow(workFlowDTO);
    };

    @Operation(
            summary = "Get All Work Flows",
            description = "Get All work flows," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "GET")
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WorkFlowDTO>> getAllWorkFlows(){
        return workFlowService.getAllWorkFlows();
    };

    @Operation(
            summary = "Get Work Flow By Id",
            description = "Get work flow by it's id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "GET")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkFlowDTO> getWorkFlowById(@PathVariable Long id){
        return workFlowService.getWorkFlowById(id);
    };

    @Operation(
            summary = "Update Entity Type By Work Flow Id",
            description = "Update entity type by the work flow id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "PUT")
    @PutMapping("/{id}/entity-type")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkFlowDTO> updateWorkFlowEntityTypeById(@PathVariable Long id, @RequestBody Long entityTypeId){
        return workFlowService.updateWorkFlowEntityTypeById(id, entityTypeId);
    };

    @Operation(
            summary = "Update Version By Work Flow Id",
            description = "Update version by the work flow id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "PUT")
    @PutMapping("/{id}/version")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkFlowDTO> updateWorkFlowVersionById(@PathVariable Long id, @RequestBody String version){
        return workFlowService.updateWorkFlowVersionById(id, version);
    };

    @Operation(
            summary = "Delete Work Flow By Id",
            description = "Delete work flow by it's id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "DELETE")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleterEntityTypeById(@PathVariable Long id){
        return workFlowService.deleteWorkFlowById(id);
    };



}
