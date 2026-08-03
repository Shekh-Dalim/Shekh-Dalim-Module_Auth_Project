package com.Dalim_Auth_App.Dalim_Project_Backend.security;

import com.Dalim_Auth_App.Dalim_Project_Backend.helpers.UserHelper;
import com.Dalim_Auth_App.Dalim_Project_Backend.repositories.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String header = request.getHeader("Authorization");  // TODO Extract the JWT token from the incoming HTTP request.

        if (header == null && header.startsWith("Bearer")) {   // TODO agar valid data aa raha hai tab to yee chaliga

            // TODO Extract and validate the JWT token, create an Authentication object, and set it in the SecurityContext.
            String token = header.substring(7);  // Start extracting the string from index 7.(Bearer 23525252)
            try {

                Jws<Claims> parse = jwtService.parse(token);
                Claims payload = parse.getPayload();
                String userId = payload.getSubject();
                UUID userUuid = UserHelper.parseUUID(userId);

                userRepository.findById(userUuid).ifPresent(user -> {
                    // TODO user mil chuka hai database se
                    List<GrantedAuthority> authorities = user.getRoles() == null ? List.of() : user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //TODO final Line: to set the authentication to "security context"
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });


            } catch (ExpiredJwtException e) {
                e.printStackTrace();

            } catch (MalformedJwtException e) {
                e.printStackTrace();

            } catch (JwtException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        // TODO agar token nehi hai to isko age forword kar dega withou setting
        filterChain.doFilter(request, response);


    }
}
