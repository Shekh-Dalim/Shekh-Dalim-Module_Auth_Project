package com.Dalim_Auth_App.Dalim_Project_Backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {

    @Id
    private UUID id = UUID.randomUUID();  // TODO Automatically generates a unique ID when a new Role object is created.

    @Column(unique = true, nullable = false )
    private String name; // TODO what is the roll USER , ADMIN , GUEST
}
