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
        Object firstNameValue = new BeanWrapperImpl(obj).getPropertyValue(firstName);
        Object lastNameValue = new BeanWrapperImpl(obj).getPropertyValue(lastName);

        return (String.valueOf(firstNameValue) != null || String.valueOf(lastNameValue) != null);
    }

}
