package com.Dalim_Auth_App.Dalim_Project_Backend.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    private UUID id ;
    private String name;
}
