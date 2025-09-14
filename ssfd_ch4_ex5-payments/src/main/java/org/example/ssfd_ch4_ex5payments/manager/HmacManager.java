package org.example.ssfd_ch4_ex5payments.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * HMAC manager responsible for computing hex-encoded HMACs of content.
 */
@Component
public class HmacManager {

    private static final String HMAC_ALGO_PRIMARY = "HmacSHA3-256";
    private static final String HMAC_ALGO_FALLBACK = "HmacSHA256";

    @Value("${security.hmac.secret}")
    private String hmacSecret;

    /**
     * Compute HMAC (hex) of provided bytes using the preferred algorithm with a fallback.
     */
    public String computeHmacHex(byte[] content) {
        try {
            return computeHmacHexInternal(content, HMAC_ALGO_PRIMARY);
        } catch (IllegalStateException e) {
            if (e.getCause() instanceof NoSuchAlgorithmException) {
                return computeHmacHexInternal(content, HMAC_ALGO_FALLBACK);
            }
            throw e;
        }
    }

    private String computeHmacHexInternal(byte[] content, String algo) {
        if (hmacSecret == null || hmacSecret.isBlank()) {
            throw new IllegalStateException("HMAC secret is not configured. Please set 'security.hmac.secret' in application.properties");
        }
        try {
            Mac mac = Mac.getInstance(algo);
            SecretKeySpec keySpec = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), algo);
            mac.init(keySpec);
            byte[] hmac = mac.doFinal(content);
            return HexFormat.of().formatHex(hmac);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("HMAC algorithm not available: " + algo, e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to compute HMAC", e);
        }
    }
}
