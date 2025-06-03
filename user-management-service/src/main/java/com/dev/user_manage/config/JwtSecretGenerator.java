package com.dev.user_manage.config;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public class JwtSecretGenerator {

    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS384);
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println("Clave HS384 (48 bytes):");
        System.out.println("jwt.secret=" + base64Key);
    }
}
