package fr.redby.gallery.thumbnails.strategy.image;

import fr.redby.gallery.thumbnails.strategy.AbstractImageThumbnailStrategy;
import java.nio.file.Path;
import java.util.List;

public class JpegThumbnailStrategy extends AbstractImageThumbnailStrategy {

    public JpegThumbnailStrategy() {
        super(".jpg", ".jpeg");
    }

    @Override
    protected List<String> getExternalConverterCommand(Path filePath) {
        return List.of(
                "magick", filePath.toString(),
                "-auto-orient",
                "-resize", "300x200^",
                "-gravity", "center",
                "-extent", "300x200",
                "-quality", "90",
                "jpeg:-"
        );
    }
}
