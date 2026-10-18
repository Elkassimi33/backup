package com.fst.rsi.service;

import com.fst.rsi.dtos.AuthResponseDTO;
import lombok.Getter;


@Getter
public class TokenManager {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenManager() {
    }


    public void setTokens(AuthResponseDTO authResponse) {
        if (authResponse != null) {
            this.accessToken = authResponse.getToken();
            this.refreshToken = authResponse.getRefreshToken();
            if (authResponse.getType() != null) {
                this.tokenType = authResponse.getType();
            }
        }
    }


    public void setTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


    public String getAuthorizationHeader() {
        if (accessToken == null || accessToken.isEmpty()) {
            return null;
        }
        return tokenType + " " + accessToken;
    }


    public boolean isAuthenticated() {
        return accessToken != null && !accessToken.isEmpty();
    }


    public void clearTokens() {
        this.accessToken = null;
        this.refreshToken = null;
    }

}

