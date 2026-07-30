package com.Dalim_Auth_App.Dalim_Project_Backend.repositories;

import com.Dalim_Auth_App.Dalim_Project_Backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email); // TODO Used to get the User using their email.
    boolean existsByEmail(String email);      // TODO Used to check whether the email already exists or not.


}
