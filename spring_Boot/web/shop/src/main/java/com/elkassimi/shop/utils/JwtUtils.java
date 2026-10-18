package com.elkassimi.shop.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

/**
 * JwtUtils
 */
@Component
public class JwtUtils {
    private final String SECRET = "my-secret-key-okokoadjjsnajjfej-sajklajbfvbiuwhfi-1455267868279391061!@";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_DATE = 1000 * 60 * 60;

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

}
