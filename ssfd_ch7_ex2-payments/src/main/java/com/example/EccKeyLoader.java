package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EccKeyLoader {

    /**
     * Loads a PKCS#8 EC private key from a PEM file located on the classpath (resources folder).
     * Example resourcePath: "/private_key.pem"
     */
    public static PrivateKey loadPrivateKeyFromPem(String resourcePath) throws Exception {
        URL url = EccKeyLoader.class.getResource(resourcePath);
        if (url == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);

        Path path = Paths.get(url.toURI());
        String pem = Files.readString(path, StandardCharsets.US_ASCII);
        String base64 = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] der = Base64.getDecoder().decode(base64);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePrivate(spec);
    }

    /**
     * Loads an X.509 EC public key from a PEM file located on the classpath (resources folder).
     * Example resourcePath: "/signing_public_key.pem"
     */
    public static PublicKey loadPublicKeyFromPem(String resourcePath) throws Exception {
        URL url = EccKeyLoader.class.getResource(resourcePath);
        if (url == null) throw new IllegalArgumentException("Resource not found: " + resourcePath);

        Path path = Paths.get(url.toURI());
        String pem = Files.readString(path, StandardCharsets.US_ASCII);
        String base64 = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] der = Base64.getDecoder().decode(base64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePublic(spec);
    }

    /**
     * Reads all bytes from a resource on the classpath. Returns null if not found.
     */
    public static byte[] readAllBytesFromResource(String resourcePath) throws IOException {
        try (InputStream is = EccKeyLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            return is.readAllBytes();
        }
    }
}
