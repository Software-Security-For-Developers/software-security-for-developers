package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility for storing result files into the project root directory.
 * The project root is detected by searching upwards for a pom.xml.
 * If not found, the current working directory is used as a fallback.
 */
public final class ResultWriter {
    private ResultWriter() { }

    /**
     * Writes the given content to a file located in the project root directory.
     * A trailing system-specific newline is appended to the content to match CLI expectations.
     *
     * @param fileName file name (no directories), e.g., "refunds.json.sha3"
     * @param content  content to write
     * @return the Path of the written file
     * @throws IOException on I/O error
     */
    public static Path writeToProjectRoot(String fileName, String content) throws IOException {
        Path root = detectProjectRoot();
        Path output = root.resolve(fileName);
        Files.writeString(output, content + System.lineSeparator());
        return output;
    }

    /**
     * Attempt to detect the project root by walking up from the current working directory
     * until a directory containing a pom.xml is found.
     * If none is found, returns the current working directory.
     */
    public static Path detectProjectRoot() {
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        Path p = cwd;
        while (p != null) {
            if (Files.isRegularFile(p.resolve("pom.xml"))) {
                return p;
            }
            p = p.getParent();
        }
        // Fallback
        return cwd;
    }
}
