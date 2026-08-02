
// TODO                              Rule Book (Security-এর নিয়ম বলে)

package com.Dalim_Auth_App.Dalim_Project_Backend.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // TODO Configure Spring Security rules (which APIs are public and which require login).
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/api/v1/auth/register").permitAll()
                                .requestMatchers("/api/v1/auth/login").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();

    }

    // TODO Creates a PasswordEncoder bean so Spring Security can hash passwords before saving them and verify them during login.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
