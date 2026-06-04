package fr.redby.gallery.thumbnails;

import fr.redby.gallery.thumbnails.strategy.ThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.image.HeicThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.image.JpegThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.video.MovThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.video.Mp4ThumbnailStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
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
import java.util.List;
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
@EnableCaching
public class ThumbnailsApplication {

    private static final Logger log = LoggerFactory.getLogger(ThumbnailsApplication.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final List<ThumbnailStrategy> thumbnailStrategies = List.of(
            new JpegThumbnailStrategy(),
            new HeicThumbnailStrategy(),
            new Mp4ThumbnailStrategy(),
            new MovThumbnailStrategy()
    );

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

            ThumbnailStrategy strategy = selectStrategy(imagePath);
            if (strategy == null) {
                log.warn("Unsupported media type for thumbnail: " + safeName);
                return fallbackThumbnail(album.name + " - " + name);
            }

            byte[] imageBytes = generateCachedThumbnail(year, albumId, name, imagePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error loading image", e);
            return fallbackThumbnail(note);
        }
    }

    @Cacheable(value = "thumbnails", key = "#year + ':' + #albumId + ':' + #name")
    private byte[] generateCachedThumbnail(String year, String albumId, String name, Path imagePath) throws IOException {
        return generateThumbnail(imagePath);
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
        ThumbnailStrategy strategy = selectStrategy(imagePath);
        if (strategy == null) {
            throw new IOException("No thumbnail strategy found for " + imagePath);
        }
        return strategy.generate(imagePath);
    }

    private ThumbnailStrategy selectStrategy(Path imagePath) {
        return thumbnailStrategies.stream()
                .filter(strategy -> strategy.supports(imagePath))
                .findFirst()
                .orElse(null);
    }
}
