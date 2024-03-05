package com.example.workflowmicroservice.repository;

import com.example.workflowmicroservice.model.WFLogHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogHistoryRepository extends MongoRepository<WFLogHistory, Long> {
}
