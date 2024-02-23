package com.example.customermicroservice.exceptionhandling;

import org.springframework.http.HttpStatus;


public enum ErrorsEnum {

    CUSTOMER_NOT_FOUND("Customer Not Found!"),

    CUSTOMERS_NOT_FOUND("No Customers Found!"),

    TOKEN_IS_NULL("The JWT Token Of The Request To WorkFlow Microservice Is Null!"),

    STATUS_IS_NULL("The Customer Is Not Created, The Returned Status From Work Flow Is Null!"),

    CUSTOMER_NOT_UPDATED("The Customer Is Not Updated, The Returned Status From Work Flow Is Null!"),

    DATABASE_ERROR("The Customer Is Not Created: "),

    NULL_POINTER("The Response Body Is Not Correct!");

    public final String message;

    ErrorsEnum(String message){
        this.message = message;
    }

}
