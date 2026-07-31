package com.Dalim_Auth_App.Dalim_Project_Backend.controllers;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;
import com.Dalim_Auth_App.Dalim_Project_Backend.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto){  // TODO @RequestBody is used to take data from the HTTP request body (usually JSON) and convert it into a Java object.

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.registerUser(userDto)); // TODO ResponseEntity is used to send a complete HTTP response to the client.
    }
}
