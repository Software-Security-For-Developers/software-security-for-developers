package com.example.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public final class OutputWriter {
    private OutputWriter() {}

    public static Path writeStringToProjectRoot(String fileName, String content) throws IOException {
        String userDir = System.getProperty("user.dir"); // Points to project root when app is run from there
        Path outPath = Path.of(userDir, fileName);
        Files.writeString(
                outPath,
                content == null ? "" : content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
        return outPath;
    }
}
