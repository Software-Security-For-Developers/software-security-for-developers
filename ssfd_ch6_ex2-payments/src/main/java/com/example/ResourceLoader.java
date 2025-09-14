package com.example;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility for loading classpath resources as byte arrays.
 */
public final class ResourceLoader {
    private ResourceLoader() {}

    /**
     * Reads all bytes from a classpath resource.
     *
     * @param resourcePath the classpath resource path (e.g., "/file.txt")
     * @return the bytes of the resource, or null if the resource was not found
     * @throws IOException if an I/O error occurs while reading
     */
    public static byte[] readAllBytesFromResource(String resourcePath) throws IOException {
        try (InputStream is = ResourceLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) return null;
            return is.readAllBytes();
        }
    }
}
