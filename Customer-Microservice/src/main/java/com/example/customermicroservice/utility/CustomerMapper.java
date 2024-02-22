package com.example.customermicroservice.utility;

import com.example.customermicroservice.dto.CustomerRequestDTO;
import com.example.customermicroservice.dto.CustomerResponseDTO;
import com.example.customermicroservice.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerRequestDTO mapToRequestDTO(Customer customer);

    Customer mapToEntity(CustomerRequestDTO customerRequestDTO);

    Customer mapToEntity(CustomerResponseDTO customerResponseDTO);

    CustomerResponseDTO mapToResponseDTO(Customer customer);

}
