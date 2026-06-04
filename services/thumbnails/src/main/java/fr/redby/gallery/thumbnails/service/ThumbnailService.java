package fr.redby.gallery.thumbnails.service;

import fr.redby.gallery.thumbnails.strategy.ThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.image.HeicThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.image.JpegThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.video.MovThumbnailStrategy;
import fr.redby.gallery.thumbnails.strategy.video.Mp4ThumbnailStrategy;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
public class ThumbnailService {

    private final List<ThumbnailStrategy> thumbnailStrategies = List.of(
            new JpegThumbnailStrategy(),
            new HeicThumbnailStrategy(),
            new Mp4ThumbnailStrategy(),
            new MovThumbnailStrategy()
    );

    @Cacheable(value = "thumbnails", key = "#year + ':' + #albumId + ':' + #name")
    public byte[] getThumbnail(String year, String albumId, String name, Path imagePath) throws IOException {
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
