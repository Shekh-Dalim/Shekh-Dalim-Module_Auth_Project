package com.Dalim_Auth_App.Dalim_Project_Backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder  // TODO Lombok annotation used to create objects easily using the Builder Pattern, especially when a class has many fields.
@Entity
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
    private Provider provider = Provider.LOCAL;  // TODO what is the provider like GITHUB, GOOGLE or by default LOCAL
    private Set<Role> roles = new HashSet<>(); // TODO Defines different access for different users, like ADMIN, EMPLOYEE, or USER.

}
