package fr.redby.gallery.thumbnails.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ThumbnailProcessRunner {

    private static final Logger log = LoggerFactory.getLogger(ThumbnailProcessRunner.class);

    public static byte[] runProcessToBytes(List<String> cmd) throws IOException {
        log.debug("Executing external converter: {}", String.join(" ", cmd));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectError(ProcessBuilder.Redirect.PIPE);
        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            log.error("Failed to start process: {}", String.join(" ", cmd), e);
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
                log.error("External converter interrupted: {}", String.join(" ", cmd), ie);
                throw new IOException("External converter interrupted", ie);
            }

            if (exitCode != 0) {
                log.error("Converter failed (exit {}): {} -- command: {}", exitCode, errorText, String.join(" ", cmd));
                throw new IOException("Converter failed (exit " + exitCode + "): " + errorText);
            }

            log.debug("External converter succeeded: {} ({} bytes)", String.join(" ", cmd), baos.size());
            return baos.toByteArray();
        }
    }
}
