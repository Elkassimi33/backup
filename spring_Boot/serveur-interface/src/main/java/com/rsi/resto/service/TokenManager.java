package com.rsi.resto.service;

import com.rsi.resto.dtos.AuthResponseDTO;

/**
 * Utility class to manage JWT tokens across API services
 */
public class TokenManager {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenManager() {
    }

    /**
     * Set tokens from AuthResponseDTO
     */
    public void setTokens(AuthResponseDTO authResponse) {
        if (authResponse != null) {
            this.accessToken = authResponse.getToken();
            this.refreshToken = authResponse.getRefreshToken();
            if (authResponse.getType() != null) {
                this.tokenType = authResponse.getType();
            }
        }
    }

    /**
     * Set tokens directly
     */
    public void setTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    /**
     * Get the authorization header value
     */
    public String getAuthorizationHeader() {
        if (accessToken == null || accessToken.isEmpty()) {
            return null;
        }
        return tokenType + " " + accessToken;
    }

    /**
     * Check if user is authenticated
     */
    public boolean isAuthenticated() {
        return accessToken != null && !accessToken.isEmpty();
    }

    /**
     * Clear all tokens (logout)
     */
    public void clearTokens() {
        this.accessToken = null;
        this.refreshToken = null;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }
}

