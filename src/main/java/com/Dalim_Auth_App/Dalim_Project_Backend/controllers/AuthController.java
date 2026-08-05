package com.Dalim_Auth_App.Dalim_Project_Backend.controllers;

import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.LoginRequest;
import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.RefreshTokenRequest;
import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.TokenResponse;
import com.Dalim_Auth_App.Dalim_Project_Backend.dtos.UserDto;
import com.Dalim_Auth_App.Dalim_Project_Backend.entities.RefreshToken;
import com.Dalim_Auth_App.Dalim_Project_Backend.entities.User;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.RefreshTokenRepository;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.UserRepository;
import com.Dalim_Auth_App.Dalim_Project_Backend.security.CookieService;
import com.Dalim_Auth_App.Dalim_Project_Backend.security.JwtService;
import com.Dalim_Auth_App.Dalim_Project_Backend.services.AuthService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
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
        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
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


//    ###############


    // TODO access and refresh token renule karne ke lie api banaii


    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody(required = false) RefreshTokenRequest body,
            HttpServletResponse response,
            HttpServletRequest request
    ) {

        String refreshToken = readRefreshTokenFromRequest(body, request).orElseThrow(() -> new BadCredentialsException("refresh Token is missing"));

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadCredentialsException("Invalid Refresh Token type");
        }

        String jti = jwtService.getJti(refreshToken);
        UUID userId = jwtService.getUserId(refreshToken);
        RefreshToken storedRefreshToken = refreshTokenRepository.findByJti(jti).orElseThrow(() -> new BadCredentialsException("Refresh token not recognized"));

        if (storedRefreshToken.isRevoked()) {
            throw new BadCredentialsException("Refresh token expired or revoked");
        }

        if (storedRefreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token expired");
        }

        if (!storedRefreshToken.getUser().getId().equals(userId)) {
            throw new BadCredentialsException("Refresh token does not belong to this user");
        }

        // refresh token ko rotate:
        storedRefreshToken.setRevoked(true);
        String newJti = UUID.randomUUID().toString();
        storedRefreshToken.setReplacedByToken(newJti);
        refreshTokenRepository.save(storedRefreshToken);

        User user = storedRefreshToken.getUser();
        var newRefreshTokenOb = RefreshToken.builder()
                .jti(newJti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(newRefreshTokenOb);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user, newRefreshTokenOb.getJti());

        cookieService.attachRefreshCookie(response, newRefreshToken, (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);
        return ResponseEntity.ok(TokenResponse.of(newAccessToken, newRefreshToken, jwtService.getAccessTtlSeconds(), mapper.map(user, UserDto.class)));

    }


    // TODO logout the refresh token
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        readRefreshTokenFromRequest(null, request).ifPresent((String token) -> {

            try {

                if (jwtService.isRefreshToken(token)) {
                    String jti = jwtService.getJti(token);

                    refreshTokenRepository.findByJti(jti).ifPresent((RefreshToken rt) -> {
                        rt.setRevoked(true);
                        refreshTokenRepository.save(rt);
                    });
                }

            } catch (JwtException ignored) {
            }

        });

        // Use CookieUtil (same behavior)
        cookieService.clearRefreshCookie(response);
        cookieService.addNoStoreHeaders(response);
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }



    // TODO This method will read refresh token from request header or body
    private Optional<String> readRefreshTokenFromRequest(RefreshTokenRequest body, HttpServletRequest request) {
        // 1. prefer reading refresh token from cookies
        if (request.getCookies() != null) {

            Optional<String> fromCookie = Arrays.stream(request.getCookies())
                    .filter(c -> cookieService.getRefreshTokenCookieName().equals(c.getName()))
                    .map(Cookie::getValue)
                    .filter(v -> !v.isBlank())
                    .findFirst();

            if (fromCookie.isPresent()) {
                return fromCookie;
            }

        }

        // TODO 2.Body
        if (body != null && body.refreshToken() != null && !body.refreshToken().isBlank()) {
            return Optional.of(body.refreshToken());

        }
        //3. TODO Custom header
        String refreshHeader = request.getHeader("X-Refresh-Token");

        if (refreshHeader != null && !refreshHeader.isBlank()) {
            return Optional.of(refreshHeader.trim());
        }

        //4. TODO Authorization = Bearer <token>
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {

            String candidate = authHeader.substring(7).trim();

            if (!candidate.isEmpty()) {
                try {
                    if (jwtService.isRefreshToken(candidate)) {
                        return Optional.of(candidate);
                    }
                } catch (Exception ignored) {
                    // Ignore invalid token
                }
            }
        }

        return Optional.empty();


    }


//    ###############

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {  // TODO @RequestBody is used to take data from the HTTP request body (usually JSON) and convert it into a Java object.

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto)); // TODO ResponseEntity is used to send a complete HTTP response to the client.
    }
}
