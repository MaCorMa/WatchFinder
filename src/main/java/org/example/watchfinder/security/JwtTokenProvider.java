package org.example.watchfinder.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {


    @Value("${app.jwtSecret:mySecretKey}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs:86400000}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserDetailsServiceImpl userPrincipal = (UserDetailsServiceImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        Claims claims = Jwts.parser()
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }


    public boolean validateToken(String authToken) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            ex.getMessage();
        }
        return false;
    }
}