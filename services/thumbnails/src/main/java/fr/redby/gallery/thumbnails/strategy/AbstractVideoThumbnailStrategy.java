package fr.redby.gallery.thumbnails.strategy;

import fr.redby.gallery.thumbnails.util.ThumbnailProcessRunner;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractVideoThumbnailStrategy implements ThumbnailStrategy {
    private final List<String> supportedExtensions;

    protected AbstractVideoThumbnailStrategy(String... supportedExtensions) {
        this.supportedExtensions = Arrays.asList(supportedExtensions);
    }

    @Override
    public boolean supports(Path filePath) {
        String name = filePath.getFileName().toString().toLowerCase();
        return supportedExtensions.stream().anyMatch(name::endsWith);
    }

    @Override
    public byte[] generate(Path filePath) throws IOException {
        return ThumbnailProcessRunner.runProcessToBytes(getVideoCommand(filePath));
    }

    protected List<String> getVideoCommand(Path filePath) {
        return List.of(
                "ffmpeg",
                "-y",
                "-hide_banner",
                "-loglevel", "error",
                "-i", filePath.toString(),
                "-ss", "00:00:01",
                "-vframes", "1",
                "-vf", "scale=300:200:force_original_aspect_ratio=decrease,pad=300:200:(ow-iw)/2:(oh-ih)/2",
                "-f", "image2",
                "-q:v", "2",
                "pipe:1"
        );
    }
}
