package fr.redby.gallery.servicepictures.service;

import fr.redby.gallery.servicepictures.bean.Picture;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service dedicated to the management of pictures.
 */
@Service
public class PicturesService {

    private static final Logger LOGGER = LoggerFactory.getLogger( PicturesService.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";

    /**
     * Returns all JPG pictures recursively in a given category/album.
     * @param category the category
     * @param album the album name
     * @return a list of Picture objects.
     * @throws IOException any error is returned to the calling method.
     */
    public List<Picture> listPictures(final String category, final @PathVariable String album)
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

    public void readAndDisplayMetadata( String fileName ) {

    }

}
