package com.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class EcKeyPairGenerator {

    private final String curveName;
    private final SecureRandom secureRandom;

    public EcKeyPairGenerator(String curveName, SecureRandom secureRandom) {
        this.curveName = curveName;
        this.secureRandom = secureRandom;
    }

    public KeyPair generate() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(curveName);
        kpg.initialize(ecSpec, secureRandom);
        return kpg.generateKeyPair();
    }
}
