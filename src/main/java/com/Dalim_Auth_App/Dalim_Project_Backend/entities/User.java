package com.Dalim_Auth_App.Dalim_Project_Backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// TODO Lombok annotation used to create objects easily using the Builder Pattern, especially when a class has many fields.
@Entity()
// TODO Tells JPA that this class is an entity and can be mapped to a database table. (says WHAT should be mapped.)
@Table(name = "users")
// TODO Tells JPA which database table will store the entity's data.. (says WHERE it should be mapped.)
public class User implements UserDetails {  // TODO This UserDetails comes from Spring Security, not from your project.

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "user_email", unique = true, length = 300)
    private String email;

    @Column(name = "user_name", length = 500)
    private String name;
    private String password;
    private String image;
    private boolean enable = true;    // TODO Controls account status: true = active, false = disabled

    // TODO thi two are automatically handle by jpa and hibernate
    private Instant createdAt = Instant.now();  // TODO Stores the exact date and time when the user account is created.
    private Instant updateAt = Instant.now();   // TODO Stores the date and time when the user information was last updated.

    @Enumerated(EnumType.STRING)  // TODO tells JPA/Hibernate how to store an enum value in the database.
    private Provider provider = Provider.LOCAL;  // TODO what is the provider like GITHUB, GOOGLE or by default LOCAL

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>(); // TODO Defines different access for different users, like ADMIN, EMPLOYEE, or USER.

    // TODO Entity life cycle
    @PrePersist
    // TODO (handles the FIRST SAVE (INSERT)). Sets createdAt to the current time automatically just before the entity is first saved to the database.
    protected void onCreate() {
        Instant now = Instant.now();  // TODO Instant.now() is a static factory method. It returns an Instant object to you.
        if (createdAt == null) createdAt = now;  // TODO "Does this employee already have a creation time?"
        updateAt = now;  // TODO If no creation time has been set yet, set the creation time to the current time
    }

    @PreUpdate  //TODO handles LATER CHANGES (UPDATE).
    protected void onUpdate() {
        updateAt = Instant.now();
    }





    // TODO Over ride the all UserDetails interface method

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {   // TODO getAuthorities() returns the collection of authorities (such as ROLE_ADMIN or ROLE_USER) granted to the authenticated user.

        // TODO Convert each user role into an authority that Spring Security understands.
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();


    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}
