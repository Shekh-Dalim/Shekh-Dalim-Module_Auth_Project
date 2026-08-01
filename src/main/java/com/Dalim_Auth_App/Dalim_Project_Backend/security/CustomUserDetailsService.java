package com.Dalim_Auth_App.Dalim_Project_Backend.security;

import com.Dalim_Auth_App.Dalim_Project_Backend.entities.User;
import com.Dalim_Auth_App.Dalim_Project_Backend.exceptions.ResourceNotFoundException;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// TODO It is used to load (fetch) the user from the database during login.
@Service
@RequiredArgsConstructor  // TODO Creates a constructor only for final and @NonNull fields.
public class CustomUserDetailsService implements UserDetailsService { // TODO UserDetailsService also comes from Spring Security.(implement because Because Spring Security needs a way to load a user from the database.)

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Invalid Email or Password !!"));

    }
}
