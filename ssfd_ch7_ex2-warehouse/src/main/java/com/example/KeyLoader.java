package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class KeyLoader {
    private KeyLoader() {}

    // Loads an EC public key (X.509 SubjectPublicKeyInfo) from PEM using standard JCA only.
    public static PublicKey loadEcPublicKeyFromPem(String resourceName) throws Exception {
        byte[] der = readPemDer(resourceName);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePublic(x509);
    }

    // Loads an EC private key (PKCS#8) from PEM using standard JCA only.
    public static PrivateKey loadEcPrivateKeyFromPem(String resourceName) throws Exception {
        byte[] der = readPemDer(resourceName);
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePrivate(pkcs8);
    }

    // Helper to read a PEM resource and return DER bytes (base64-decoded)
    private static byte[] readPemDer(String resourceName) throws IOException {
        byte[] pemBytes = loadResourceBytes(resourceName);
        String pem = new String(pemBytes, StandardCharsets.US_ASCII);
        pem = pem.replace("\r", "");
        StringBuilder sb = new StringBuilder();
        for (String line : pem.split("\n")) {
            if (line.startsWith("-----")) continue;
            if (line.isBlank()) continue;
            sb.append(line.trim());
        }
        return Base64.getDecoder().decode(sb.toString());
    }

    public static byte[] loadResourceBytes(String resourceName) throws IOException {
        ClassLoader cl = KeyLoader.class.getClassLoader();
        try (InputStream is = cl.getResourceAsStream(resourceName)) {
            if (is == null) throw new IOException("Resource not found: " + resourceName);
            return is.readAllBytes();
        }
    }
}
