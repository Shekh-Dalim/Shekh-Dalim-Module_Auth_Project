package com.Dalim_Auth_App.Dalim_Project_Backend.controllers;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.LoginRequest;
import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.TokenResponse;
import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;
import com.Dalim_Auth_App.Dalim_Project_Backend.entities.RefreshToken;
import com.Dalim_Auth_App.Dalim_Project_Backend.entities.User;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.RefreshTokenRepository;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.UserRepository;
import com.Dalim_Auth_App.Dalim_Project_Backend.security.CookieService;
import com.Dalim_Auth_App.Dalim_Project_Backend.security.JwtService;
import com.Dalim_Auth_App.Dalim_Project_Backend.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper mapper;
    private final CookieService cookieService;


    // TODO Login method means create the token
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        // authenticate
        Authentication authentication = authenticate(loginRequest);
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new BadCredentialsException("Invalid Username or password"));
        if (!user.isEnable()) { // if disable
            throw new DisabledException("User is disable");
        }

        String jti = UUID.randomUUID().toString();
        var refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        //save the information of the refresh token
        refreshTokenRepository.save(refreshTokenOb);


        // access token -- generate token If enable then generate jwt
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());

        //TODO Use cookie service to attach refresh token in cookie
        cookieService.attachRefreshCookie(response,refreshToken,(int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);

        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(), mapper.map(user, UserDto.class));
        return ResponseEntity.ok(tokenResponse);

    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Username or password !!");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {  // TODO @RequestBody is used to take data from the HTTP request body (usually JSON) and convert it into a Java object.

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto)); // TODO ResponseEntity is used to send a complete HTTP response to the client.
    }
}
