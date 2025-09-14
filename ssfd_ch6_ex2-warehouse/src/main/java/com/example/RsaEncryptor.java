package com.example;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;


public final class RsaEncryptor {
    private RsaEncryptor() {}
    
    public static byte[] rsaEncryptChunked(byte[] data, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        int modulusBytes = ((RSAPublicKey) key).getModulus().bitLength();
        modulusBytes = (modulusBytes + 7) / 8;
        int maxBlock = modulusBytes - 11; // PKCS#1 v1.5 overhead

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int off = 0; off < data.length; off += maxBlock) {
            int len = Math.min(maxBlock, data.length - off);
            out.write(cipher.doFinal(data, off, len));
        }
        return out.toByteArray();
    }
}
