package com.example;

import java.io.IOException;
import java.io.InputStream;

public final class ResourceLoader {
    private ResourceLoader() {}

    public static byte[] loadResourceBytes(String resourceName) throws IOException {
        ClassLoader cl = ResourceLoader.class.getClassLoader();
        try (InputStream is = cl.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourceName);
            }
            return is.readAllBytes();
        }
    }
}
