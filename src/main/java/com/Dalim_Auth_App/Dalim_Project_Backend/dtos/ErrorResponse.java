package com.Dalim_Auth_App.Dalim_Project_Backend.dtos;

import org.springframework.http.HttpStatus;

public record ErrorResponse(   // TODO ErrorResponse is a type — specifically, in your code it is a Java record type.
                               String message,
                               HttpStatus status,
                               int statusCode

) {

}
