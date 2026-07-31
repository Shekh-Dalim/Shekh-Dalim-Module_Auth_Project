package com.Dalim_Auth_App.Dalim_Project_Backend.services.impl;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;
import com.Dalim_Auth_App.Dalim_Project_Backend.entities.Provider;
import com.Dalim_Auth_App.Dalim_Project_Backend.entities.User;
import com.Dalim_Auth_App.Dalim_Project_Backend.exceptions.ResourceNotFoundException;
import com.Dalim_Auth_App.Dalim_Project_Backend.helpers.UserHelper;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.UserRepository;
import com.Dalim_Auth_App.Dalim_Project_Backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    // TODO All DB operations succeed together; if one fails, all changes are rolled back. (EX Example: Bank transfer — if money is deducted from A but adding to B fails, the deduction is rolled back.)
    public UserDto createUser(UserDto userDto) {

        // 1st: TODO Is an email provided?
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is Required");
        }

        // 2nd:TODO Check whether this email is already registered to prevent duplicate user accounts.
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with given Email Already exists");
        }

        // if you have extra checks __put here
        // 3rd: TODO Convert the form into a database Entity
        User user = modelMapper.map(userDto, User.class);
        user.setProvider(    // TODO If the user provides a provider, use it. If the user doesn't provide one, use LOCAL by default.
                userDto.getProvider() != null
                        ? userDto.getProvider()
                        : Provider.LOCAL
        );

        // TODO we assign role for authorization

        // 4th: TODO Save the User Entity into the database and store the returned saved User object in savedUser.
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given email id"));
        return modelMapper.map(user, UserDto.class);  // (source object, convert intoUserDto) TODO Convert the User Entity object into a UserDto object and return that DTO.
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        // TODO we are not going to change email id for this project
        if (userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getImage() != null) existingUser.setImage(userDto.getImage());
        if (userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        // TODO : Change the password updation logic...
        if (userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.isEnable());
        existingUser.setUpdateAt(Instant.now());
        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = UserHelper.parseUUID(userId);  // TODO Convert the userId from String to UUID and store it in uId.
        User user = userRepository.findById(uId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        userRepository.delete(user);

    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(() -> new ResourceNotFoundException("ser not found with given id"));
        return modelMapper.map(user, UserDto.class);
    }


    @Override
    @Transactional(readOnly = true)
    //TODO This transaction is intended only for reading data from the database, not changing it.
    //TODO Get all users from the database → convert every User Entity into UserDto → put them into a List → return the list.
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
