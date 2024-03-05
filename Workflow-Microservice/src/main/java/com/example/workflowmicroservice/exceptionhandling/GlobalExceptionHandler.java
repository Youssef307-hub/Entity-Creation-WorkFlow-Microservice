package com.example.workflowmicroservice.exceptionhandling;

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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorObject> handleRuntimeException(RuntimeException exception) {
        ErrorObject errorResponse = ErrorObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorObject> handleNullPointerException(NullPointerException exception) {
        ErrorObject errorResponse = ErrorObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getLocalizedMessage())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorObject> handleDataBaseException(DataAccessException exception) {

        String errorMessage = exception.getMostSpecificCause().getMessage();

        ErrorObject errorResponse = ErrorObject.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(errorMessage)
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handelValidationException(MethodArgumentNotValidException exception) {

        List<String> errorResponse = new ArrayList<>();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> errorResponse.add(error.getDefaultMessage()));

        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(errorResponse.toString())
                .build();

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorObject> handelNotFoundException(ObjectNotFoundException exception) {

        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .TimeStamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

}
