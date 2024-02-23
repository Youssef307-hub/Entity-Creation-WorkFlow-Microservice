package com.example.customermicroservice.service;

import com.example.customermicroservice.dto.CustomerRequestDTO;
import com.example.customermicroservice.dto.CustomerResponseDTO;
import com.example.customermicroservice.exceptionhandling.*;
import com.example.customermicroservice.model.Customer;
import com.example.customermicroservice.repository.CustomerRepository;
import com.example.customermicroservice.utility.CustomerMapper;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static com.example.customermicroservice.exceptionhandling.ErrorsEnum.*;

@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final RestTemplate restTemplate;

    public CustomerService(RestTemplateBuilder restTemplateBuilder, CustomerMapper customerMapper, CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Transactional
    public ResponseEntity<CustomerResponseDTO> createCustomer(CustomerRequestDTO customerRequestDTO){
        // Map the incoming DTO to customer
        Customer customer = null;
        try {
            customer = customerMapper.mapToEntity(customerRequestDTO);
            LOGGER.info("The Customer in creation step is {} ", customer);
        } catch (NullPointerException e) {
            throw new NullPointerException(NULL_POINTER.message);
        }
        // Set the initial data for every customer
        customer.setCreatedBy(extractUserName());
        customer.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        customer.setStatus("Draft");
        LOGGER.info("The Customer in creation step after setting initial data is {} ", customer);

        // Save the Customer with the initial data to generate id
        customerRepository.save(customer);
        LOGGER.info("The Customer in creation step after being saved is {} ", customer);

        // Url to hit in the WorkFlow Microservice
        String url = "http://localhost:8080/work-flow/initiate/Customer/" + customer.getId();

        // Create request entity including the JWT token in the header to be sent along with the request
        HttpEntity<String> requestEntity = createRequestEntityWithHeaders();

        // The response entity body will contain the status based on the logged-in user
        String status = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            status = response.getBody();
        } catch (RestClientException e) {
            customerRepository.deleteById(customer.getId());
            throw new RuntimeException(e);
        }

        // If Status returned with null value delete the initial data created for the customer
        if (status == null){
            customerRepository.deleteById(customer.getId());
            throw new NullPointerException(STATUS_IS_NULL.message);
        }

        // Change the initial status then save the updated customer
        customer.setStatus(status);
        customerRepository.save(customer);

         return new ResponseEntity<>(customerMapper.mapToResponseDTO(customer), HttpStatus.CREATED);
    }


    public ResponseEntity<List<CustomerResponseDTO>> getPendingCustomers(){

        // Url to hit in the WorkFlow Microservice
        String url = "http://localhost:8080/work-flow/pending/Customer";

        // Create request entity including the JWT token in the header to be sent along with the request
        HttpEntity<String> requestEntity = createRequestEntityWithHeaders();

        // The response entity body will contain the list of ids of the customers in any step that's
        // not the last step of creation
        List<Long> entityIds = null;
        try {
            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>(){});
            entityIds = response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        // If there is no ids returned then throw exception
        if(entityIds == null || entityIds.isEmpty()){
            throw new CustomerNotFoundException(CUSTOMERS_NOT_FOUND.message);
        }

        // Getting the customers with the returned ids
        List<Customer> customers = customerRepository.findAllByIdIn(entityIds);

        return new ResponseEntity<>(customers.stream().map(customerMapper::mapToResponseDTO).toList(), HttpStatus.OK);
    }


    public ResponseEntity<List<CustomerResponseDTO>> getApprovedCustomers(){

        // Url to hit in the WorkFlow Microservice
        String url = "http://localhost:8080/work-flow/approved/Customer";

        // Create request entity including the JWT token in the header to be sent along with the request
        HttpEntity<String> requestEntity = createRequestEntityWithHeaders();

        // The response entity body will contain the list of ids of the customers in the last step of creation AKA Approved
        List<Long> entityIds = null;
        try {
            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<>(){});

            entityIds = response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        // If there is no ids returned then throw exception
        if(entityIds == null || entityIds.isEmpty()){
            throw new CustomerNotFoundException(CUSTOMERS_NOT_FOUND.message);
        }

        // Getting the customers with the returned ids
        List<Customer> customers = customerRepository.findAllByIdIn(entityIds);

        return new ResponseEntity<>(customers.stream().map(customerMapper::mapToResponseDTO).toList(), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<CustomerResponseDTO> updateCustomerStatus(Long entityId){
        Customer customer = customerRepository.findById(entityId)
                .orElseThrow(()-> new CustomerNotFoundException(CUSTOMER_NOT_FOUND.message));
        LOGGER.info("The Customer is {} ", customer);

        // Url to hit in the WorkFlow Microservice
        String url = "http://localhost:8080/work-flow/update/Customer/" + customer.getId();

        // Create request entity including the JWT token in the header to be sent along with the request
        HttpEntity<String> requestEntity = createRequestEntityWithHeaders();

        // The response entity body will contain the new status after updating the customer
        String status = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
            status = response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }

        // If Status returned with null value Customer Will Not Be Updated
        if (status == null){
            throw new NullPointerException(CUSTOMER_NOT_UPDATED.message);
        }

        // Setting the new status and the modification date coming from the WF Microservice
        customer.setStatus(status);
        customer.setLastModificationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        // Updating the customer
        customerRepository.save(customer);

        return new ResponseEntity<>(customerMapper.mapToResponseDTO(customer), HttpStatus.CREATED);
    }

    private HttpEntity<String> createRequestEntityWithHeaders() {
        // Get the JWT token from the SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String token = "Bearer " + jwt.getTokenValue();

        if(jwt.getTokenValue() == null){
            throw new NullPointerException(ErrorsEnum.TOKEN_IS_NULL.message);
        }

        // Create headers and set the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity with the headers
        return new HttpEntity<>(headers);
    }

    private String extractUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        return jwt.getClaim("preferred_username");
    }

}
