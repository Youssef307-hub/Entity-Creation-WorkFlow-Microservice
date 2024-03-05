package com.example.workflowmicroservice.model;

public enum StatusEnum {

    PENDING("Pending"),

    Approved("Approved");

    public String value;

    StatusEnum(String value){
        this.value = value;
    }
}
