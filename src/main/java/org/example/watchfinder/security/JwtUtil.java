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
            //throw e;
            throw new RuntimeException("Failed to generate token", e);
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

        } catch (SignatureException ex) {
            System.err.println("Invalid JWT signature: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.err.println("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.err.println("JWT token is expired: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.err.println("JWT token is unsupported: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string is empty: " + ex.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exception during parsing/validation
            System.err.println("An unexpected error occurred during JWT validation: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Token is invalid due to one of the caught exceptions
    }
}