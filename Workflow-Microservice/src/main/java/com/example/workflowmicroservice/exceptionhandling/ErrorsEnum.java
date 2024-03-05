package com.example.workflowmicroservice.exceptionhandling;

public enum ErrorsEnum {
    ENTITY_TYPE_NOT_FOUND("Entity Type Not Found!"),

    WORK_FLOW_NOT_FOUND("Work Flow Not Found!"),

    LOG_NOT_FOUND("Work Flow Log Not Found!"),

    STEP_NOT_FOUND("Work Flow Step Not Found!"),

    NULL_POINTER("Check Your Input Again!"),

    BAD_REQUEST("The Request Body Is Not Matching With The Attribute!"),

    NO_SUCH_ATTRIBUTE("No Such Attribute!");

    public final String message;

    ErrorsEnum(String message){
        this.message = message;
    }
}
