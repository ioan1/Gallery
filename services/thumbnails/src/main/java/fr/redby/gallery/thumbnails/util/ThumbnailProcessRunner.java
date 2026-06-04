package fr.redby.gallery.thumbnails.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ThumbnailProcessRunner {

    public static byte[] runProcessToBytes(List<String> cmd) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectError(ProcessBuilder.Redirect.PIPE);
        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new IOException("Failed to start process: " + String.join(" ", cmd), e);
        }

        try (InputStream stdout = process.getInputStream(); InputStream stderr = process.getErrorStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = stdout.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }

            String errorText = new String(stderr.readAllBytes());
            int exitCode;
            try {
                exitCode = process.waitFor();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new IOException("External converter interrupted", ie);
            }

            if (exitCode != 0) {
                throw new IOException("Converter failed (exit " + exitCode + "): " + errorText);
            }

            return baos.toByteArray();
        }
    }
}
