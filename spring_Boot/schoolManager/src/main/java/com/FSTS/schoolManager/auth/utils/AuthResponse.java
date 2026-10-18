package com.FSTS.schoolManager.auth.utils;

import com.FSTS.schoolManager.auth.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private Integer userId;
    private User.UserRole userRole;

}
