package com.example.workflowmicroservice.exceptionhandling;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(){}

    public ObjectNotFoundException(String message){
        super(message);
    }
}
