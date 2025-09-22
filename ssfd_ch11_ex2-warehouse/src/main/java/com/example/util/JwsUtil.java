package com.example.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;

import java.util.Base64;


public final class JwsUtil {

    private static final byte[] HMAC_SECRET = Base64.getDecoder().decode(
            "VGhpc0lzQS1EZW1vLVNlY3JldC1LZXktRm9yLUhTMjU2LUF0TGVhc3QtMzJCeXRlcyE="
    );

    public static String generateJws(String payloadJson) throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256)
                .contentType("application/json")
                .build();
        JWSObject jwsObject = new JWSObject(header, new Payload(payloadJson));
        jwsObject.sign(new MACSigner(HMAC_SECRET));
        return jwsObject.serialize();
    }

}
