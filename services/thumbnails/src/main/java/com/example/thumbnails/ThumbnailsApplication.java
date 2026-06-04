package com.example.thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@RestController
public class ThumbnailsApplication {

    private static final Logger log = LoggerFactory.getLogger(ThumbnailsApplication.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(ThumbnailsApplication.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\"}";
    }

    @GetMapping("/")
    public String index() {
        return "{\"service\":\"Thumbnails\",\"version\":\"1.0.0\"}";
    }

    @GetMapping(value = "/thumbnails/small", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> small(@RequestParam(required = false) String note) throws IOException {
        byte[] bytes = renderImage(note);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/thumbnails/small/{year}/{albumId}/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> smallPath(@PathVariable String year, @PathVariable String albumId, @PathVariable String name,
                                            @RequestHeader(value = "Authorization", required = false) String authorization,
                                            @RequestParam(required = false) String note) throws IOException {
        try {
            // Fetch album info from albums service to get album name
            String albumsServiceUrl = System.getenv("ALBUMS_SERVICE_URL");
            if (albumsServiceUrl == null) {
                albumsServiceUrl = "http://service-albums:8000";
            }

            String url = albumsServiceUrl + "/albums/" + year;
            HttpHeaders requestHeaders = new HttpHeaders();
            if (authorization != null && !authorization.isEmpty()) {
                requestHeaders.set("Authorization", authorization);
            }
            HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);
            ResponseEntity<Album[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Album[].class);
            Album[] albums = response.getBody();

            if (albums == null || albums.length == 0) {
                return fallbackThumbnail(note);
            }

            Album album = java.util.Arrays.stream(albums)
                    .filter(a -> a.id.equals(albumId))
                    .findFirst()
                    .orElse(null);

            if (album == null) {
                return fallbackThumbnail(note);
            }

            String albumDate = album.date != null ? album.date.format(DateTimeFormatter.BASIC_ISO_DATE) : "";
            String albumFolderName = albumDate + " - " + album.name;
            String safeName = Paths.get(name).getFileName().toString();
            // Reconstruct path: /photos/<year>/<album-date> - <albumName>/<name>
            Path imagePath = Paths.get("/photos", year, albumFolderName, safeName);

            if (!Files.exists(imagePath)) {
                log.warn("Image not found: " + imagePath);
                return fallbackThumbnail(album.name + " - " + name);
            }

            String lowerName = safeName.toLowerCase();
            if (!lowerName.endsWith(".jpg") && !lowerName.endsWith(".jpeg") && !lowerName.endsWith(".heic")) {
                log.warn("Unsupported image type for thumbnail: " + safeName);
                return fallbackThumbnail(album.name + " - " + name);
            }

            try {
                byte[] imageBytes = generateThumbnail(imagePath);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentLength(imageBytes.length);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            } catch (IOException e) {
                log.error("Error generating thumbnail for " + imagePath, e);
                return fallbackThumbnail(album.name + " - " + name);
            }

        } catch (Exception e) {
            log.error("Error loading image", e);
            return fallbackThumbnail(note);
        }
    }

    private ResponseEntity<byte[]> fallbackThumbnail(String note) throws IOException {
        byte[] bytes = renderImage(note);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    private byte[] renderImage(String note) throws IOException {
        int width = 150;
        int height = 100;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        try {
            java.util.concurrent.ThreadLocalRandom rnd = java.util.concurrent.ThreadLocalRandom.current();
            Color bg = new Color(rnd.nextInt(0, 256), rnd.nextInt(0, 256), rnd.nextInt(0, 256));
            g.setColor(bg);
            g.fillRect(0, 0, width, height);

            g.setColor(bg.darker());
            g.drawRect(0, 0, width - 1, height - 1);

            int rectW = 36;
            int rectH = 28;
            g.setColor(bg.darker().darker());
            g.fillRect(8, (height - rectH) / 2, rectW, rectH);

            double luminance = (0.2126 * bg.getRed() + 0.7152 * bg.getGreen() + 0.0722 * bg.getBlue()) / 255.0;
            Color textColor = luminance < 0.5 ? Color.WHITE : Color.BLACK;

            g.setColor(textColor);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            String text = "Thumb";
            FontMetrics fm = g.getFontMetrics();
            int tx = 8 + rectW + 6;
            int ty = (height - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(text, tx, ty);

            if (note != null && !note.isEmpty()) {
                g.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g.drawString(note, tx, ty + fm.getHeight());
            }
        } finally {
            g.dispose();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", baos);
        return baos.toByteArray();
    }

    private byte[] generateThumbnail(Path imagePath) throws IOException {
        try {
            return generateThumbnailWithImageMagick(imagePath);
        } catch (IOException e) {
            log.warn("ImageMagick thumbnail generation failed, falling back to Java resize", e);
            BufferedImage source = readImage(imagePath);
            BufferedImage thumbnail = createThumbnail(source, 600, 400);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, "jpeg", baos);
            return baos.toByteArray();
        }
    }

    private byte[] generateThumbnailWithImageMagick(Path imagePath) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "magick", "convert",
                imagePath.toString(),
                "-auto-orient",
                "-resize", "300x200^",
                "-gravity", "center",
                "-extent", "600x400",
                "-quality", "90",
                "jpeg:-"
        );
        pb.redirectError(ProcessBuilder.Redirect.PIPE);

        Process process = pb.start();
        try (InputStream stdout = process.getInputStream(); InputStream stderr = process.getErrorStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
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
                throw new IOException("ImageMagick thumbnail generation interrupted", ie);
            }

            if (exitCode != 0) {
                throw new IOException("ImageMagick convert failed (exit " + exitCode + "): " + errorText);
            }

            return baos.toByteArray();
        }
    }

    private BufferedImage readImage(Path imagePath) throws IOException {
        try (InputStream is = Files.newInputStream(imagePath)) {
            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new IOException("Unable to read image: " + imagePath);
            }
            return image;
        }
    }

    private BufferedImage createThumbnail(BufferedImage source, int width, int height) {
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
