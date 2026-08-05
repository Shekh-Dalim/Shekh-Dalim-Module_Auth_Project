
// TODO                              Rule Book (Security-এর নিয়ম বলে)

package com.Dalim_Auth_App.Dalim_Project_Backend.configuration;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.ApiError;
import com.Dalim_Auth_App.Dalim_Project_Backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // TODO Configure Spring Security rules (which APIs are public and which require login).
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/api/v1/auth/register").permitAll()
                                .requestMatchers("/api/v1/auth/login").permitAll()
                                .requestMatchers("/api/v1/auth/refresh").permitAll()
                                .requestMatchers("/api/v1/auth/logout").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, e) -> {
                    // TODO Error message niche Bala code tab challenge jab ap ka koi esa person jo  unauthenticated hai and oo api access Carne ka try kar raha hai
//                    e.printStackTrace();
                    response.setStatus(401);
                    response.setContentType("application/json");
                    String message = e.getMessage();
                    String error = (String) request.getAttribute("error");
                    if (error != null) {
                        message = error;
                    }


//                    Map<String, Object> errorMap = Map.of("message", message, "statusCode", 401);
                    var apiError = ApiError.of(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Access", message, request.getRequestURI(), true);
                    var objectMapper = new ObjectMapper(); // TODO Let the compiler automatically determine the variable's type
                    response.getWriter().write(objectMapper.writeValueAsString(apiError));
                }))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();

    }

    // TODO Creates a PasswordEncoder bean so Spring Security can hash passwords before saving them and verify them during login.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();

    }


    // TODO In-Memory Authentication means: The username and password are stored in the application's memory (RAM), not in a database. This method is not overriding UserDetailsService. It is creating a UserDetailsService bean.
//    @Bean
//    public UserDetailsService users(){
//
//        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//
//        UserDetails user1 = userBuilder.username("Dalim").password("abc").roles("ADMIN").build(); // TODO Create a UserDetails object with username "ankit", password "abc", and role "ADMIN".
//        UserDetails user2 = userBuilder.username("Ruhul").password("xyz").roles("ADMIN").build();
//        UserDetails user3 = userBuilder.username("Rahul").password("xyz").roles("USER").build();
//        return new InMemoryUserDetailsManager(user1,user2,user3);  // TODO InMemoryUserDetailsManager is used to store and manage user details in memory (RAM) instead of a database.
//    }


}
