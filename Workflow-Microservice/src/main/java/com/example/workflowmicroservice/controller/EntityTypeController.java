package com.example.workflowmicroservice.controller;

import com.example.workflowmicroservice.dto.EntityTypeDTO;
import com.example.workflowmicroservice.service.EntityTypeService;
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
@RequestMapping("/type")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Entity Type Creation", description = "Entity Type Management APIs")
public class EntityTypeController {

    private final EntityTypeService typeService;

    @Operation(
            summary = "Create New Entity Type",
            description = "Create new entity type," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "POST")
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityTypeDTO> createEntityType(@Valid @RequestBody EntityTypeDTO entityTypeDTO){
      return typeService.createEntityType(entityTypeDTO);
    };

    @Operation(
            summary = "Get All Entity Types",
            description = "Get All entity types," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "GET")
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EntityTypeDTO>> getAllEntityTypes(){
        return typeService.getAllEntityTypes();
    };

    @Operation(
            summary = "Get Entity Type By Id",
            description = "Get entity type by it's id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "GET")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityTypeDTO> getEntityTypeById(@PathVariable Long id){
        return typeService.getEntityTypeById(id);
    };

    @Operation(
            summary = "Update Entity Type Name By Id",
            description = "Update entity type name by the entity id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "PUT")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityTypeDTO> updateEntityTypeById(@PathVariable Long id, @RequestBody EntityTypeDTO entityTypeDTO){
        return typeService.updateEntityTypeById(id, entityTypeDTO);
    };

    @Operation(
            summary = "Delete Entity Type By Id",
            description = "Delete entity type by the entity id," +
                    " the Api can be accessed by any user that has role 'admin'",
            tags = "DELETE")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleterEntityTypeById(@PathVariable Long id){
        return typeService.deleteEntityTypeById(id);
    };
}
