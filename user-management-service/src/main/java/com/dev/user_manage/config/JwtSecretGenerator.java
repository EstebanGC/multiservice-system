package com.dev.user_manage.config;

import java.security.SecureRandom;
import java.util.HexFormat;

public class JwtSecretGenerator {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        random.nextBytes(key);
        String secret = HexFormat.of().formatHex(key);
        System.out.println("New jwt.secret = " + secret);
    }
}
