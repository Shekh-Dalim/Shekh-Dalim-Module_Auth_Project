package com.Dalim_Auth_App.Dalim_Project_Backend.dtos;

import com.Dalim_Auth_App.Dalim_Project_Backend.entities.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String email;
    private String name;
    private String password;
    private String image;
    private boolean enable = true;
    private Instant createdAt = Instant.now();
    private Instant updateAt = Instant.now();
    private Provider provider = Provider.LOCAL;
    private Set<RoleDto> roles = new HashSet<>();

}
