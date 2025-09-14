package com.example;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.io.ByteArrayOutputStream;

public class RsaDecryptor {

    public static byte[] rsaDecryptChunked(byte[] ciphertext, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int keySizeBytes = (((RSAPrivateKey) privateKey).getModulus().bitLength() + 7) /8;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (int offset = 0; offset < ciphertext.length;) {
                int len = Math.min(keySizeBytes, ciphertext.length - offset);
                baos.write(cipher.doFinal(ciphertext, offset, len));
                offset += len;
            }
            return baos.toByteArray();
        }
    }
}
