package com.FSTS.schoolManager.service;

import com.FSTS.schoolManager.auth.entities.RefreshToken;
import com.FSTS.schoolManager.auth.entities.User;
import com.FSTS.schoolManager.auth.repositories.RefreshTokenRepository;
import com.FSTS.schoolManager.auth.repositories.UserRepository;
import com.FSTS.schoolManager.auth.service.JwtService;
import com.FSTS.schoolManager.auth.service.RefreshTokenService;
import com.FSTS.schoolManager.auth.utils.AuthResponse;
import com.FSTS.schoolManager.auth.utils.LoginRequest;
import com.FSTS.schoolManager.auth.utils.PasswordResetRequest;
import com.FSTS.schoolManager.exception.AccountLockedException;
import com.FSTS.schoolManager.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    public AuthResponse login(LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            if (!user.isAccountNonLocked()) {
                throw new AccountLockedException("Account is locked due to multiple failed login attempts");
            }

            // Record successful login
            user.recordSuccessfulLogin();
            userRepository.save(user);

            String accessToken = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());

            return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .userId(user.getId())
                .userRole(user.getRole())
                .build();
        } catch (LockedException e) {
            throw new AccountLockedException("Account is locked due to multiple failed login attempts");
        }
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
            .ifPresent(token -> {
                refreshTokenRepository.delete(token);
                User user = token.getUser();
                user.setRefreshToken(null);
                userRepository.save(user);
            });
    }

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        user.setPasswordResetToken(token);
        user.setPasswordResetExpires(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        // Send password reset email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");
        message.setText(String.format(
            "Dear %s,\n\n" +
            "You have requested to reset your password. Please use the following token to reset your password:\n\n" +
            "Token: %s\n\n" +
            "This token will expire in 24 hours.\n\n" +
            "If you did not request this password reset, please ignore this email.\n\n" +
            "Best regards,\n" +
            "School Management System",
            user.getUsername(),
            token
        ));
        
        mailSender.send(message);
    }

    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByPasswordResetToken(request.getToken())
            .orElseThrow(() -> new InvalidTokenException("Invalid or expired password reset token"));

        if (!user.isPasswordResetTokenValid()) {
            throw new InvalidTokenException("Password reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.invalidatePasswordResetToken();
        userRepository.save(user);
    }

    public void unlockAccount(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.unlockAccount();
        userRepository.save(user);
    }
}
