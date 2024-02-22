package com.example.workflowmicroservice.controller;

import com.example.workflowmicroservice.service.WFLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/work-flow")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Work FLow Microservice Logic", description = "Work Flow Logic Management APIs")
public class WFLogicController {

    private final WFLogicService logicService;

    @Operation(
            summary = "Assign Status To New Entity",
            description = "Assigning status to new entities and put the right status of that entity based on the logged-in user role," +
                    " the Api can be accessed by any user that has role 'user' or above",
            tags = "POST")
    @PostMapping("/initiate/{entityType}/{entityId}")
    public String initiateWorkFlow(@PathVariable String entityType, @PathVariable Long entityId){

        return logicService.initiateWorkFlow(entityType,entityId);
    }

    @Operation(
            summary = "Get All Pending Entities Of Certain Type",
            description = "Getting all entities that their step number is less than the maximum number of steps for entity creation," +
                    " the Api can be accessed by any user that has role 'user' or above",
            tags = "GET")
    @GetMapping("/pending/{entityType}")
    public List<Long> getPendingEntitiesIds(@PathVariable String entityType){

        return logicService.getPendingEntitiesIds(entityType);
    }

    @Operation(
            summary = "Get All Approved Entities Of Certain Type",
            description = "Getting all entities that their step number is equal to the maximum number of steps for entity creation," +
                    " the Api can be accessed by any user that has role 'user' or above",
            tags = "GET")
    @GetMapping("/approved/{entityType}")
    public List<Long> getApprovedEntitiesIds(@PathVariable String entityType){

        return logicService.getApprovedEntitiesIds(entityType);
    }

    @Operation(
            summary = "Update Status Of Existing Entity",
            description = "Updating the status of existing entities and put the right status of that entity based on the logged-in user role," +
                    " the Api can be accessed by any user that has role 'user' or above",
            tags = "PUT")
    @PutMapping("/update/{entityType}/{entityId}")
    public String updateEntityStatusById(@PathVariable String entityType, @PathVariable Long entityId){

        return logicService.updateEntityStatusById(entityType, entityId);
    }

}
