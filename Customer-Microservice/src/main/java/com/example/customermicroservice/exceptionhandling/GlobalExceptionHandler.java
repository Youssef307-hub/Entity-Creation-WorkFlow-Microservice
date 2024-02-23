package com.example.customermicroservice.exceptionhandling;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import static com.example.customermicroservice.exceptionhandling.ErrorsEnum.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGlobalException() {
        ErrorObject errorResponse = ErrorObject.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal Server Error")
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorObject> handleDataBaseException(DataAccessException exception) {

        String errorMessage = exception.getMostSpecificCause().getMessage();

        ErrorObject errorResponse = ErrorObject.builder()
                .statusCode(DATABASE_ERROR.status.value())
                .message(DATABASE_ERROR.message + errorMessage)
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        return new ResponseEntity<>(errorResponse, DATABASE_ERROR.status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handelValidationException(MethodArgumentNotValidException exception){

        List<String> errorResponse = new ArrayList<>();

        exception.getBindingResult().
                getAllErrors().
                forEach(error -> errorResponse.add(error.getDefaultMessage()));

        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(errorResponse.toString())
                .build();

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorObject> handelCustomerNotFoundException(){
        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(CUSTOMER_NOT_FOUND.status.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(CUSTOMER_NOT_FOUND.message)
                .build();

        return new ResponseEntity<>(errorObject, CUSTOMER_NOT_FOUND.status);
    }

    @ExceptionHandler(CustomersNotFoundException.class)
    public ResponseEntity<ErrorObject> handelCustomersNotFoundException(){
        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(CUSTOMERS_NOT_FOUND.status.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(CUSTOMERS_NOT_FOUND.message)
                .build();

        return new ResponseEntity<>(errorObject, CUSTOMERS_NOT_FOUND.status);
    }

    @ExceptionHandler(TokenIsNullException.class)
    public ResponseEntity<ErrorObject> handelTokenIsNullException(){
        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(TOKEN_IS_NULL.status.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(TOKEN_IS_NULL.message)
                .build();

        return new ResponseEntity<>(errorObject, TOKEN_IS_NULL.status);
    }

    @ExceptionHandler(StatusIsNullException.class)
    public ResponseEntity<ErrorObject> handelStatusIsNullException(){
        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(STATUS_IS_NULL.status.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(STATUS_IS_NULL.message)
                .build();

        return new ResponseEntity<>(errorObject, STATUS_IS_NULL.status);
    }

    @ExceptionHandler(CustomerNotUpdatedException.class)
    public ResponseEntity<ErrorObject> handelCustomerNotUpdatedException(){
        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(CUSTOMER_NOT_UPDATED.status.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(CUSTOMER_NOT_UPDATED.message)
                .build();

        return new ResponseEntity<>(errorObject, CUSTOMER_NOT_UPDATED.status);
    }

}

