package com.dev.user_manage.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey generateSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateJwtToken(String username) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(generateSigningKey())
                .compact();
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(generateSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public String extractSubject(String jwtToken) {
        return extractClaims(jwtToken).getSubject();
    }

    public Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken).getExpiration();
    }

    public boolean isTokenValid(String jwtToken) {
        return new Date().before(extractExpiration(jwtToken));
    }
}
