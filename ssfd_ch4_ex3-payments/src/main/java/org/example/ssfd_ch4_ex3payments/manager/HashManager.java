package org.example.ssfd_ch4_ex3payments.manager;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class HashManager {

    private static final String ALGO = "SHA3-256";

    public String computeSha3Hex(byte[] content) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGO);
            byte[] digest = md.digest(content);
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // Should not happen on modern JDKs
            throw new IllegalStateException("SHA3 algorithm not available", e);
        }
    }

    public String computeSha3Hex(String content) {
        return computeSha3Hex(content.getBytes(StandardCharsets.UTF_8));
    }

}
