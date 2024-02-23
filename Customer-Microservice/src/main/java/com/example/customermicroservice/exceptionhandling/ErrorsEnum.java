package com.example.customermicroservice.exceptionhandling;

import org.springframework.http.HttpStatus;


public enum ErrorsEnum {

    CUSTOMER_NOT_FOUND("Customer Not Found!", HttpStatus.NOT_FOUND),

    CUSTOMERS_NOT_FOUND("No Customers Found!", HttpStatus.NOT_FOUND),

    TOKEN_IS_NULL("The JWT Token Of The Request To WorkFlow Microservice Is Null!", HttpStatus.BAD_REQUEST),

    STATUS_IS_NULL("The Customer Is Not Created, The Returned Status From Work Flow Is Null!", HttpStatus.BAD_REQUEST),

    CUSTOMER_NOT_UPDATED("The Customer Is Not Updated, The Returned Status From Work Flow Is Null!", HttpStatus.BAD_REQUEST),

    DATABASE_ERROR("The Customer Is Not Created: ", HttpStatus.INTERNAL_SERVER_ERROR);

    public final String message;
    public final HttpStatus status;

    ErrorsEnum(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

}
