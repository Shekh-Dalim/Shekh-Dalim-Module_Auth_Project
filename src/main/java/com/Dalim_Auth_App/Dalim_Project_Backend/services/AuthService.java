package com.Dalim_Auth_App.Dalim_Project_Backend.services;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;

public interface AuthService {

    UserDto registerUser(UserDto userDto);

    // TODO login user
}
