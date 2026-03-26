package com.anuj.onlineVoting.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final String SECRET = "this-is-my-secret-key-this-is-my-secret-key";

    public Key getSigningkey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String createToken(Map<String, Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setHeaderParam("type", "jwt")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(getSigningkey())
                .compact();
    }

    public String extractUserName(String token){
        return extractAllClaims(token).getSubject();
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenValid(String token){
        return !isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

}
