package com.FSTS.schoolManager.auth.service;

import com.FSTS.schoolManager.exception.InvalidJwtException;
import com.FSTS.schoolManager.exception.JwtExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    private static final String SECRET_KEY="s7F3q1R9XyV2pB8zWtN5cGvJ7mK4hL6nQ0eP1iUo9dA2bM3lZ5x";

//    extract  username from jwt

    public String extractUsername(String token){
        return  extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    extract information from jwt

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSengInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {

            log.warn("JWT token has expired: {}", e.getMessage());
            throw new JwtExpiredException("JWT token has expired");
        } catch (JwtException e) {
            log.error("JWT parsing error: {}", e.getMessage());
            throw new InvalidJwtException("Invalid JWT token");
        }
    }
//    decode and get key

    private Key getSengInKey(){
//        decode private SECRET_KEY
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

//    generate token using jwt utility class and return token as String

    public String generateToken(Map<String,Object> extraClaims,UserDetails userDetails){

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+2000*60*24))
                .signWith(getSengInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    if token is valid by checking if token is expired for current user

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (JwtExpiredException | InvalidJwtException e) {
            return false;
        }
    }

    //    if token is expired
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtExpiredException | InvalidJwtException e) {
            return true;
        }
    }
    //    get expiration date from token
    private Date extractExpiration(String token) {

        return extractClaim(token,Claims::getExpiration);
    }


}
