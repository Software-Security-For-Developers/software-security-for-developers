package com.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;


public class RsaKeyPairGenerator {

    private final int keySize;
    private final SecureRandom secureRandom;

    public RsaKeyPairGenerator(int keySize, SecureRandom secureRandom) {
        this.keySize = keySize;
        this.secureRandom = secureRandom;
    }

    public KeyPair generate() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(keySize, secureRandom);
        return kpg.generateKeyPair();
    }
}
