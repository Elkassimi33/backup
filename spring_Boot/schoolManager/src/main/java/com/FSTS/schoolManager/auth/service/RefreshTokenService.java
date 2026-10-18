package com.FSTS.schoolManager.auth.service;


import com.FSTS.schoolManager.auth.entities.RefreshToken;
import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.auth.repositories.RefreshTokenRepository;
import com.FSTS.schoolManager.auth.repositories.UserRepository;
import com.FSTS.schoolManager.exception.RefreshTokenException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username){

        User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found with email:"+username));

        RefreshToken refreshToken= user.getRefreshToken();

        if(refreshToken==null){
            long refreshTokenValidity=5*3600*10000;
            refreshToken= RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);

        }
        return  refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){

        RefreshToken refToken= refreshTokenRepository.findByRefreshToken(refreshToken )
                .orElseThrow(()->new RefreshTokenException("Refresh Token not found!"));

        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RefreshTokenException("Refresh token expired");
        }
        return refToken;
    }
}

