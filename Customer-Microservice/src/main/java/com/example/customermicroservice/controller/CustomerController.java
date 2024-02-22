package com.example.customermicroservice.controller;

import com.example.customermicroservice.dto.CustomerRequestDTO;
import com.example.customermicroservice.dto.CustomerResponseDTO;
import com.example.customermicroservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Customer Microservice", description = "Customer Management APIs")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Create New Customer",
            description = "Create new customer object and put the status of that customer based on the logged-in user role," +
                    " the Api can be accessed by any user that has role 'user' or above",
            tags = "POST")
    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){

        return customerService.createCustomer(customerRequestDTO);
    }

    @Operation(
            summary = "Update Existing Customer Status",
            description = "Update customer status of the customer based on the logged-in user role," +
                    " the Api can be accessed by any user that has role 'manager' or 'admin'",
            tags = "PUT")
    @PutMapping("/update/{entityId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<CustomerResponseDTO> updateCustomerStatus(@PathVariable Long entityId){

        return customerService.updateCustomerStatus(entityId);
    }

    @Operation(
            summary = "Get Pending Customers",
            description = "Get List of pending customers who's their status is not equal the maximum creation step," +
                    " the Api can be accessed by any user that has role 'manager' or 'admin'",
            tags = "GET")
    @GetMapping("/pending")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<CustomerResponseDTO>> getPendingCustomers(){

        return customerService.getPendingCustomers();
    }

    @Operation(
            summary = "Get Created Customers",
            description = "Get List of pending customers who's their status is equal the maximum creation step," +
                    " the Api can be accessed by any user that has role 'user' or above",
            tags = "GET")
    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CustomerResponseDTO>> getApprovedCustomers(){

        return customerService.getApprovedCustomers();
    }


}
