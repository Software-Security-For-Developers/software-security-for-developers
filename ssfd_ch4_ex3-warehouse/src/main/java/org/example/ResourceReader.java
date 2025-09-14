package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility for reading resources from the classpath.
 */
public final class ResourceReader {
    private ResourceReader() { }

    /**
     * Locate a resource by name using the context ClassLoader.
     * @param resourceName classpath resource name
     * @return URL of the resource
     * @throws IOException if resource is not found
     */
    public static URL getResourceUrl(String resourceName) throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = cl.getResource(resourceName);
        if (resourceUrl == null) {
            throw new IOException("Resource not found on classpath: " + resourceName);
        }
        return resourceUrl;
    }

    /**
     * Read all bytes from the provided resource URL.
     * @param resourceUrl URL returned by getResourceUrl
     * @return bytes of the resource
     * @throws IOException on I/O error
     */
    public static byte[] readAllBytes(URL resourceUrl) throws IOException {
        try (InputStream is = resourceUrl.openStream()) {
            return is.readAllBytes();
        }
    }
}
