package fr.redby.gallery.servicepictures.service;

import fr.redby.gallery.servicepictures.bean.Picture;
import fr.redby.gallery.servicepictures.repositories.ExifDataRepository;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service dedicated to the management of pictures.
 */
@Service
public class PicturesService {

    private static final Logger LOGGER = LoggerFactory.getLogger( PicturesService.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";

    @Autowired
    private ExifDataRepository exifDataRepository;

    /**
     * Returns all JPG pictures recursively in a given category/album.
     * @param category the category
     * @param album the album name
     * @return a list of Picture objects.
     */
    public List<Picture> listPictures(final String category, final String album) {
        File directory = new File(System.getProperty(GALLERY_PATH));
        LOGGER.info("gallery directory: {}", directory);
        File folder = new File(directory, category + File.separator + album);
        LOGGER.info("Listing all pictures located in {}, exists={}.", folder.getAbsolutePath(), folder.exists());

        if (folder.isDirectory()) {
            try (Stream<Path> stream = Files.walk(folder.toPath())) {
                return stream
                        .map(p -> p.toFile())
                        .filter(f -> f.isFile())
                        .filter(f->f.getName().toLowerCase().endsWith(".jpg"))
                        .peek(f -> LOGGER.debug(f.getAbsolutePath()))
                        .map(f -> new Picture(category, album, f))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                LOGGER.error("Error when listing pictures.", e);
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
    }
    }

    /**
     * Returns a thumbnail of the picture.
     * @param picture the picture file
     * @param size the thumbnail size (square)
     * @return a table of bytes representing a JPG of the thumbnail.
     * @throws IOException any IO error is returned to the calling method.
     */
    public byte[] getSmall(final File picture, final int size) throws IOException {
        LOGGER.info("Reading the file {}, exists={}.", picture.getAbsolutePath(), picture.exists());

        Jedis jedis = new Jedis("redis");
        LOGGER.info("Getting response from the cache server: {}", jedis.ping());

        byte[] cached = jedis.get(picture.getAbsolutePath().getBytes());
        if (cached != null) {
            LOGGER.info("Return cached thumbnail for {}.", picture.getAbsolutePath());
            return cached;
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(picture)
                    .size(size, size)
                    .crop(Positions.CENTER)
                    .keepAspectRatio(true)
                    .outputFormat("jpg")
                    .toOutputStream(out);
            byte[] res = out.toByteArray();

            LOGGER.info("Caching the thumbnail for {}.", picture.getAbsolutePath());
            jedis.set(picture.getAbsolutePath().getBytes(), res);
            return out.toByteArray();
        }
    }

    /**
     * Returns a medium sized image of the picture.
     * @param picture the picture file
     * @return a table of bytes representing a JPG of the thumbnail.
     * @throws IOException any IO error is returned to the calling method.
     */
    public byte[] getMedium(final File picture) throws IOException {
        LOGGER.info("Reading the file {}, exists={}.", picture.getAbsolutePath(), picture.exists());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(picture)
                .size(1080, 1080)
                .keepAspectRatio(true)
                .outputFormat("jpg")
                .toOutputStream(out);

        return out.toByteArray();
    }
}
