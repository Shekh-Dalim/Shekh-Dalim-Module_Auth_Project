package com.Dalim_Auth_App.Dalim_Project_Backend.repositories;

import com.Dalim_Auth_App.Dalim_Project_Backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface userRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email); // TODO Finds a user by email; returns the User if found, otherwise returns an empty Optional.
    boolean existsByEmail(String email); // TODO Checks whether a user with this email exists; returns true if found, otherwise false.


}
