package com.example.customermicroservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = FirstOrLastNameNotNullValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FirstOrLastNameNotNull {

    String firstName();
    String lastName();

    String message() default "Please enter your first or last name, both can't be empty!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FirstOrLastNameNotNull[] value();
    }
}
