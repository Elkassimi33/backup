package com.FSTS.schoolManager.controller;

import com.FSTS.schoolManager.auth.entities.RefreshToken;
import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.auth.service.JwtService;
import com.FSTS.schoolManager.auth.service.RefreshTokenService;
import com.FSTS.schoolManager.auth.utils.*;
import com.FSTS.schoolManager.exception.JwtExpiredException;
import com.FSTS.schoolManager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
//        String refreshToken = request.getRefreshToken();
//
//        try {
//            if (jwtService.isTokenValid(refreshToken)) {
//                String username = jwtService.extractUsername(refreshToken);
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                String newAccessToken = jwtService.generateToken(userDetails);
//
//                return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken));
//            }
//        } catch (JwtExpiredException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse("Refresh token expired. Please login again."));
//        }
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(new ApiResponse("Invalid refresh token"));
//    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        authService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password-reset/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unlock-account")
    public ResponseEntity<Void> unlockAccount(@RequestParam String username) {
        authService.unlockAccount(username);
        return ResponseEntity.ok().build();
    }
}
