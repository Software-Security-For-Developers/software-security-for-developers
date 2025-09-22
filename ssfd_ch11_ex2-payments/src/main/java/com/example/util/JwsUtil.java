package com.example.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.MACVerifier;

import java.text.ParseException;
import java.util.Base64;


public final class JwsUtil {

    private static final byte[] HMAC_SECRET = Base64.getDecoder().decode(
            "VGhpc0lzQS1EZW1vLVNlY3JldC1LZXktRm9yLUhTMjU2LUF0TGVhc3QtMzJCeXRlcyE="
    );

    public static boolean verifyJws(String compactJws) {
        try {
            JWSObject parsed = JWSObject.parse(compactJws);
            return parsed.verify(new MACVerifier(HMAC_SECRET));
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }
}
