package com.example;

import java.security.SecureRandom;
import java.util.Base64;

public class Main {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static void main(String[] args) {
        byte [] bytes = generateRandomBytes(255);
        String base64 = Base64.getEncoder().encodeToString(bytes);
        System.out.println(base64);
    }

    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
}