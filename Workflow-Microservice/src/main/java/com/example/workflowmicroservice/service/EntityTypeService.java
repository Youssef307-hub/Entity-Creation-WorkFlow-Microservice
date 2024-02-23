package com.example.workflowmicroservice.service;

import com.example.workflowmicroservice.dto.EntityTypeDTO;
import com.example.workflowmicroservice.exceptionhandling.ObjectNotFoundException;
import com.example.workflowmicroservice.model.EntityType;
import com.example.workflowmicroservice.repository.EntityTypeRepository;
import com.example.workflowmicroservice.utility.WorkFlowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntityTypeService {

    private final EntityTypeRepository typeRepository;
    private final WorkFlowMapper mapper;

    @Transactional
    public ResponseEntity<EntityTypeDTO> createEntityType(EntityTypeDTO entityTypeDTO){
        typeRepository.save(mapper.mapToEntity(entityTypeDTO));

        return new ResponseEntity<>(entityTypeDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<List<EntityTypeDTO>> getAllEntityTypes(){
        List<EntityType> entityTypes = typeRepository.findAll();

        if (entityTypes.isEmpty()){
            throw new ObjectNotFoundException("Entity Types");
        }

        List<EntityTypeDTO> entityTypesDTO = entityTypes.stream().map(mapper::mapToDTO).toList();

        return new ResponseEntity<>(entityTypesDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<EntityTypeDTO> getEntityTypeById(Long id){
        EntityType entityType = typeRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Entity Type"));

        return new ResponseEntity<>(mapper.mapToDTO(entityType), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<EntityTypeDTO> updateEntityTypeById(Long id, EntityTypeDTO entityTypeDTO){
        EntityType entityType = typeRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Entity Type"));
        entityType.setTypeName(entityTypeDTO.getTypeName());
        typeRepository.save(entityType);

        return new ResponseEntity<>(mapper.mapToDTO(entityType), HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> deleteEntityTypeById(Long id){
        EntityType entityType = typeRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException("Entity Type"));
        typeRepository.delete(entityType);

        return new ResponseEntity<>(
                "Entity Type : " + mapper.mapToDTO(entityType).getTypeName() + " deleted successfully",
                HttpStatus.OK);
    }

}