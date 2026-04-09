package com.anuj.onlineVoting.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private static final String SECRET = "this-is-my-secret-key-this-is-my-secret-key";

    public Key getSigningkey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username, String role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username, role);
    }

    public String createToken(Map<String, Object> claims, String username, String role){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setHeaderParam("type", "jwt")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(getSigningkey())
                .compact();
    }

    public String createToken(Map<String, Object> claims, String username, ObjectId pollId){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setHeaderParam("type", "jwt")
                .setHeaderParam("pollId", pollId.toHexString())
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

    public ObjectId getPollId(String token){
        Object pollId =  Jwts.parserBuilder()
                .setSigningKey(getSigningkey())
                .build()
                .parseClaimsJws(token)
                .getHeader()
                .get("pollId");
        return new ObjectId(pollId.toString());
    }

    public Boolean isTokenValid(String token){
        return !isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Date getExpirationTime(String token){
        return extractAllClaims(token).getExpiration();
    }

    public Set<String> getPosts(String token){
        Claims claims = extractAllClaims(token);

        Set<String> keys = new HashSet<>(claims.keySet());

        keys.remove("sub");
        keys.remove("iat");
        keys.remove("exp");

        return keys;
    }

}
