package com.Dalim_Auth_App.Dalim_Project_Backend.services.impl;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;
import com.Dalim_Auth_App.Dalim_Project_Backend.services.AuthService;
import com.Dalim_Auth_App.Dalim_Project_Backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service  // TODO Spring automatically creates a UserServiceImpl object and keeps it in the Spring Container.
@AllArgsConstructor
// TODO Registers a new user by calling UserService.createUser() and returns the created user's data as UserDto.
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // Logic
        // varify the email
        // varify the pass
        // default roles
        //

        userDto.setPassword(passwordEncoder.encode((userDto.getPassword()))); // TODO Encode the user's password before saving it to the database.
        return userService.createUser(userDto);

    }
}

