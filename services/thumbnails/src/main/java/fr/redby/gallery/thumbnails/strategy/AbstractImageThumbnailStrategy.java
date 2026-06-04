package fr.redby.gallery.thumbnails.strategy;

import fr.redby.gallery.thumbnails.util.ThumbnailProcessRunner;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractImageThumbnailStrategy implements ThumbnailStrategy {
    private final List<String> supportedExtensions;
    private static final Logger log = LoggerFactory.getLogger(AbstractImageThumbnailStrategy.class);

    protected AbstractImageThumbnailStrategy(String... supportedExtensions) {
        this.supportedExtensions = Arrays.asList(supportedExtensions);
    }

    @Override
    public boolean supports(Path filePath) {
        String name = filePath.getFileName().toString().toLowerCase();
        return supportedExtensions.stream().anyMatch(name::endsWith);
    }

    @Override
    public byte[] generate(Path filePath) throws IOException {
        try {
            return generateViaExternalConverter(filePath);
        } catch (IOException e) {
            log.warn("External converter failed for {}, falling back to Java resize: {}", filePath, e.getMessage());
            BufferedImage source = readImage(filePath);
            BufferedImage thumbnail = createThumbnail(source, 300, 200);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(thumbnail, "jpeg", baos);
                return baos.toByteArray();
            }
        }
    }

    protected byte[] generateViaExternalConverter(Path filePath) throws IOException {
        return ThumbnailProcessRunner.runProcessToBytes(getExternalConverterCommand(filePath));
    }

    protected abstract List<String> getExternalConverterCommand(Path filePath);

    protected BufferedImage readImage(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath)) {
            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new IOException("Unable to read image: " + filePath);
            }
            return image;
        }
    }

    protected BufferedImage createThumbnail(BufferedImage source, int width, int height) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        double scale = Math.min((double) width / sourceWidth, (double) height / sourceHeight);
        int targetWidth = Math.max(1, (int) Math.round(sourceWidth * scale));
        int targetHeight = Math.max(1, (int) Math.round(sourceHeight * scale));

        BufferedImage thumbnail = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnail.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            int x = (width - targetWidth) / 2;
            int y = (height - targetHeight) / 2;
            g.drawImage(source, x, y, targetWidth, targetHeight, null);
        } finally {
            g.dispose();
        }
        return thumbnail;
    }
}
