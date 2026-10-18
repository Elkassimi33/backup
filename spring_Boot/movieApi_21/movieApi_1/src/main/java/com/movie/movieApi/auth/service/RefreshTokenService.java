package com.movie.movieApi.auth.service;

import com.movie.movieApi.auth.entities.RefreshToken;
import com.movie.movieApi.auth.entities.User;
import com.movie.movieApi.auth.epositories.RefreshTokenRepository;
import com.movie.movieApi.auth.epositories.UserRepository;
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

        User user=userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found with email:"+username));

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
                .orElseThrow(()->new RuntimeException("Refresh Token not found!"));

        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh token expired");
        }
        return refToken;
    }
}
