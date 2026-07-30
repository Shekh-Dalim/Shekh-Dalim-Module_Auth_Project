package com.Dalim_Auth_App.Dalim_Project_Backend.controllers;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;
import com.Dalim_Auth_App.Dalim_Project_Backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;  // TODO why final? -> Keeps the injected UserService reference fixed after initialization.

    @PostMapping
    // Create User Api    TODO Receives user data, sends it to the Service to create the user, and returns the created user with HTTP 201 OK.
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {   // TODO @RequestBody Takes JSON data from the HTTP request body and converts it into a Java object.
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));  //TODO ResponseEntity<UserDto> is the return type of the method.
    }

    // TODO get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {   // TODO @PathVariable is used to take a value directly from the URL path and put it into a Java variable.
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    //TODO Update User
    // TODO api/v1/users/{userId}
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.updateUser(userDto, userId));
    }

    //TODO Delete User
    // TODO api/v1/users/{userId}
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }


    // TODO Get user by ID
    // TODO /api/v1/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }


    // TODO get all user api
    @GetMapping
    public ResponseEntity<Iterable<UserDto>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());  // TODO ResponseEntity.ok(data); TODO Returns data to the client with HTTP status 200 OK.
    }


}
