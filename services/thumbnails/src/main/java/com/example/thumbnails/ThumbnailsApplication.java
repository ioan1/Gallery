package com.example.thumbnails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@RestController
public class ThumbnailsApplication {

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
                                            @RequestParam(required = false) String note) throws IOException {
        try {
            // Fetch album info from albums service to get album name
            String albumsServiceUrl = System.getenv("ALBUMS_SERVICE_URL");
            if (albumsServiceUrl == null) {
                albumsServiceUrl = "http://service-albums:8000";
            }

            String url = albumsServiceUrl + "/albums/" + year;
            Album[] albums = restTemplate.getForObject(url, Album[].class);

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

            // Reconstruct path: /photos/<year>/<album_name>/<name>
            Path imagePath = Paths.get("/photos", year, album.name, name);

            // Check if file exists
            if (!Files.exists(imagePath)) {
                return fallbackThumbnail(album.name + " - " + name);
            }

            // Read and return the real image
            byte[] imageBytes = Files.readAllBytes(imagePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
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
}
