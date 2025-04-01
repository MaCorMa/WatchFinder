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
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
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

        Date now = new Date();
        Date expire = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expire)
                .signWith(key())
                .compact();
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