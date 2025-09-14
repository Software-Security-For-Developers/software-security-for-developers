package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility for reading resources from the classpath.
 * Prefers java.nio.file.Files where possible, with a safe fallback to stream reading
 * for environments where the resource is inside a JAR.
 */
public final class ResourceReader {
    private ResourceReader() {}

    /**
     * Reads the entire contents of a classpath resource into a byte array.
     * @param resourceName the resource name relative to the classpath root (e.g., "refunds.json")
     * @return bytes of the resource, or null if not found
     * @throws IOException on I/O errors
     */
    public static byte[] readResourceFully(String resourceName) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) cl = ResourceReader.class.getClassLoader();
        URL url = cl.getResource(resourceName);
        if (url == null) return null;

        // Prefer Files.readAllBytes if we can resolve to a regular file system path
        try {
            URI uri = url.toURI();
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                Path path = Paths.get(uri);
                return Files.readAllBytes(path);
            }
        } catch (URISyntaxException ignored) {
            // Fall through to stream-based reading
        }

        // Fallback for jar or other schemes
        try (InputStream is = cl.getResourceAsStream(resourceName)) {
            if (is == null) return null;
            return is.readAllBytes();
        }
    }
}
