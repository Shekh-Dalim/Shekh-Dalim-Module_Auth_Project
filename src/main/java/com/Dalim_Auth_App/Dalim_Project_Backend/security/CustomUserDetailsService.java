// TODO                      (Login-এর সময় Database থেকে User খুঁজে আনে)

package com.Dalim_Auth_App.Dalim_Project_Backend.security;
import com.Dalim_Auth_App.Dalim_Project_Backend.entities.User;
import com.Dalim_Auth_App.Dalim_Project_Backend.exceptions.ResourceNotFoundException;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    // TODO Loads the user from the database during login.  // Login করার সময় Database থেকে User খুঁজে বের করা
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Invalid Email or Password !!"));

    }
}
