package com.example;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class KeyLoader {
    private KeyLoader() {}

    public static PublicKey loadPublicKeyFromPem(String resourceName) throws Exception {
        byte[] pemBytes = ResourceLoader.loadResourceBytes(resourceName);

        String pem = new String(pemBytes, StandardCharsets.US_ASCII);
        pem = pem.replace("\r", "");
        StringBuilder sb = new StringBuilder();
        for (String line : pem.split("\n")) {
            if (line.startsWith("-----")) continue;
            if (line.isBlank()) continue;
            sb.append(line.trim());
        }
        byte[] der = Base64.getDecoder().decode(sb.toString());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    public static PrivateKey loadPrivateKeyFromPem(String resourceName) throws Exception {
        byte[] pemBytes = ResourceLoader.loadResourceBytes(resourceName);
        String pem = new String(pemBytes, StandardCharsets.US_ASCII);
        pem = pem.replace("\r", "");
        StringBuilder sb = new StringBuilder();
        for (String line : pem.split("\n")) {
            if (line.startsWith("-----")) continue;
            if (line.isBlank()) continue;
            sb.append(line.trim());
        }
        byte[] der = Base64.getDecoder().decode(sb.toString());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }
}
