package com.Dalim_Auth_App.Dalim_Project_Backend.services;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;

public interface UserService {

    // TODO Create User
    UserDto createUser(UserDto userDto);

    // TODO Create User by email
    UserDto getUserByEmail(String email);

    // TODO update User by id
    UserDto updateUser(UserDto userDto,String userId );

    // TODO delete User
    void deleteUser(String userId);


    // TODO get User by id
    UserDto getUserById(String userId );

    // TODO get all user
    Iterable<UserDto> getAllUsers();  // TODO getAllUsers() returns multiple UserDto objects that you can iterate through one by one.




}
