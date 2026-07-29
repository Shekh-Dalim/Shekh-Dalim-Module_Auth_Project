package com.Dalim_Auth_App.Dalim_Project_Backend.entities;

import jakarta.persistence.Id;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

public class User {

    @Id
    private UUID id;

    private String email;
    private String name;
    private String password;
    private String image;
    private boolean enable = true;    // TODO Controls account status: true = active, false = disabled
    private Instant createdAt = Instant.now();  // TODO Stores the exact date and time when the user account is created.
    private Instant updateAt = Instant.now();   // TODO Stores the date and time when the user information was last updated.


    private Provider provider;

    private Set<Role> roles = new HashSet<>();

}
