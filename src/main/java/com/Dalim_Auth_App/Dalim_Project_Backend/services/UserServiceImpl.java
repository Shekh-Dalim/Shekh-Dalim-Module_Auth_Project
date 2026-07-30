package com.Dalim_Auth_App.Dalim_Project_Backend.services;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Override
    public UserDto createUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto createUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        return null;
    }

    @Override
    public UserDto deleteUserById(String userId) {
        return null;
    }

    @Override
    public UserDto getUserById(String userId) {
        return null;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return null;
    }
}
