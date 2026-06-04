package fr.redby.gallery.thumbnails.strategy;

import java.io.IOException;
import java.nio.file.Path;

public interface ThumbnailStrategy {
    boolean supports(Path filePath);
    byte[] generate(Path filePath) throws IOException;
}
