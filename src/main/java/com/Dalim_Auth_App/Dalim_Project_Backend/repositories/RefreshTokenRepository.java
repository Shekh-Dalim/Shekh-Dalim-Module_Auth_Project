package com.Dalim_Auth_App.Dalim_Project_Backend.repositories;

import com.Dalim_Auth_App.Dalim_Project_Backend.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByJti(String jti);



}
