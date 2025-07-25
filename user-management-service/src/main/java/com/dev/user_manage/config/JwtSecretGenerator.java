package com.dev.user_manage.config;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretGenerator {

    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Key HS256 (32 bytes):");
        System.out.println("jwt.secret=" + base64Key);
    }
}
