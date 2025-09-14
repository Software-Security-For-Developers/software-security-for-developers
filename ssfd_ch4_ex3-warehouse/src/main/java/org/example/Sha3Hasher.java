package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Utility for computing SHA3 hashes.
 */
public final class Sha3Hasher {
    private Sha3Hasher() { }

    /**
     * Compute SHA3-256 hash of the given data and return hex string.
     */
    public static String hashHex(byte[] data) throws NoSuchAlgorithmException {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA3-256").digest(data));
        
    }
}
