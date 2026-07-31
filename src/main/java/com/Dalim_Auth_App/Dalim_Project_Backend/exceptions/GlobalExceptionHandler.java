package com.Dalim_Auth_App.Dalim_Project_Backend.exceptions;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // TODO This annotation tells Spring that the class contains global exception handlers:
// TODO is used to globally handle exceptions that occur across all REST controllers in the project.
public class GlobalExceptionHandler {

    // TODO ResourceNotFoundException handler :: method
    @ExceptionHandler(ResourceNotFoundException.class) //ResourceNotFoundException.class means TODO If an exception of type ResourceNotFoundException occurs, handle it using the method below.
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {

        ErrorResponse internalServerError = new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND, 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(internalServerError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {

        ErrorResponse internalServerError = new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(internalServerError);
    }

}
