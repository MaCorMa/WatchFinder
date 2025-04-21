package org.example.watchfinder.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private SecretKey key(){

        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal =  (UserDetails) authentication.getPrincipal();

        String roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return generateTokenFromUsername(userPrincipal.getUsername(), roles);

    }

    public String generateTokenFromUsername(String username, String roles) {
       /*
        Date now = new Date();
        Date expire = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expire)
                .signWith(key())
                .compact();
        */
        try {
            System.out.println("Generating token for username: " + username);
            System.out.println("With roles: " + roles);

            Date now = new Date();
            Date expire = new Date(now.getTime() + jwtExpirationInMs);

            String token = Jwts.builder()
                    .subject(username)
                    .claim("roles", roles)
                    .issuedAt(now)
                    .expiration(expire)
                    .signWith(key())
                    .compact();

            System.out.println("Token generated successfully");
            return token;
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public String getUserNameFromJwtToken(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(key())
                .build();

        Claims claims = parser.parseSignedClaims(token)
                .getPayload(); // Obtiene el payload

        return claims.getSubject();
    }

    public boolean validateToken(String authToken){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(key())
                    .build();

            parser.parseSignedClaims(authToken);
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }
}