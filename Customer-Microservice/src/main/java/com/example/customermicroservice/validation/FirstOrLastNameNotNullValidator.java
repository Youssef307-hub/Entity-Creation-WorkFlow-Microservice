package com.example.customermicroservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FirstOrLastNameNotNullValidator implements ConstraintValidator<FirstOrLastNameNotNull, Object> {

    String firstName;

    String lastName;

    @Override
    public void initialize(FirstOrLastNameNotNull constraintAnnotation) {
        this.firstName = constraintAnnotation.firstName();
        this.lastName = constraintAnnotation.lastName();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
        Object firstNameValue = beanWrapper.getPropertyValue(firstName);
        Object lastNameValue = beanWrapper.getPropertyValue(lastName);

        return ((firstNameValue != null && !String.valueOf(firstNameValue).trim().isEmpty()) ||
                (lastNameValue != null && !String.valueOf(lastNameValue).trim().isEmpty()));
    }

}
