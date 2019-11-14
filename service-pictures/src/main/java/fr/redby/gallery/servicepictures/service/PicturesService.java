package fr.redby.gallery.servicepictures.service;

import fr.redby.gallery.servicepictures.bean.Picture;
import fr.redby.gallery.servicepictures.repositories.ExifDataRepository;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @throws IOException any error is returned to the calling method.
     */
    public List<Picture> listPictures(final String category, final String album)
            throws IOException {
        File directory = new File(System.getProperty(GALLERY_PATH));
        LOGGER.info("gallery directory: {}", directory);
        File folder = new File(directory, category + File.separator + album);
        LOGGER.info("Listing all pictures located in {}, exists={}.", folder.getAbsolutePath(), folder.exists());

        if (folder.isDirectory()) {
            return Files.walk(folder.toPath())
                    .map(p -> p.toFile())
                    .filter(f -> f.isFile())
                    .filter(f->f.getName().toLowerCase().endsWith(".jpg"))
                    .peek(f -> LOGGER.debug(f.getAbsolutePath()))
                    .map(f -> new Picture(category, album, f))
                    .collect(Collectors.toList());
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

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(picture)
                .size(size, size)
                .crop(Positions.CENTER)
                .keepAspectRatio(true)
                .outputFormat("jpg")
                .toOutputStream(out);

        return out.toByteArray();
    }

}
