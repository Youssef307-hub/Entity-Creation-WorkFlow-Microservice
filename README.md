# Spring Boot Entity Creation Workflow Microservice

This repository contains a Spring Boot microservice designed to handle the approval workflow for entity creation. The workflow is dynamic, with the number of steps depending on the entity type, and the approval status determined by the user's role. Below are the key features and functionalities of the microservice:

## Workflow Logic
- Entity creation workflow is based on user role and entity type.
- For example, a customer entity requires 3 steps, while an order entity requires 2 steps.
- User roles (normal user, manager, admin) influence the initial status (draft, pending, approved) of the entity.

## User Role Actions
- Admins can update the status of the customer entity to approved from any step lower.
- Managers can update the status of the customer entity to pending from any step lower.

## Microservice Architecture
- Microservices are independent, and the failure of one does not affect others.
- Services communicate through REST APIs using RestTemplate.
- Database-level and input-level validations are implemented.

## Entity Creation Workflow Management
- Entity types and workflows for each type are configurable.
- Each workflow consists of multiple steps, and the current step for each entity is saved to workflow log table.
- The previous workflow logs are persisted in a NoSQL database, and only new update logs are saved in the workflow log table.

## Authentication and Authorization
- JWT tokens with OAuth2 are used for authentication and authorization.
- Token validation occurs on the Keycloak server, and token creation is managed by the Keycloak server.
- User roles and details are stored in the Keycloak server.

## Exception Handling
- Robust exception handling mechanisms are in place to capture and manage exceptions.

## Data Transfer Objects (DTOs)
- DTOs are utilized to shield users from direct interaction with the database.

## Object-Relational Mapping (ORM)
- ORM is implemented using Spring Data JPA and Hibernate.

## Getting Started
1. Clone the repository: `git clone https://github.com/your-username/your-repo.git`
2. Configure the application properties, including Keycloak settings and database details.
3. Build and run the Spring Boot application.
4. Access the APIs for entity creation workflow management.
