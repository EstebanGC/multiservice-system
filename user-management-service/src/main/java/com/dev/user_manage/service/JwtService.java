package com.dev.user_manage.service;

import com.dev.user_manage.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateJwtToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getRoles().stream().map(Enum::name).toList());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86_400_000)) // 24h
                .signWith(signingKey, SignatureAlgorithm.HS384)
                .compact();
    }

    private Claims extractClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }


    public String extractSubject(String jwtToken) {
        return extractClaims(jwtToken).getSubject();
    }

    //check later
    public Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken).getExpiration();
    }

    public boolean isTokenValid(String jwtToken) {
        try {
            extractClaims(jwtToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
