package fr.redby.gallery.servicepictures.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.redby.gallery.servicepictures.bean.ExifData;
import fr.redby.gallery.servicepictures.bean.Picture;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
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

    /**
     * TODO.
     * @param file
     * @return
     * @throws IOException
     * @throws ImageProcessingException
     */
    public byte[] getExifData(final File file) throws IOException, ImageProcessingException {
        LOGGER.info("getExifData({})", file.getAbsolutePath());
        File stored = getExifStoredMetadata(file);
        if (stored.exists()) {
            LOGGER.info("The file exists, reading all bytes.");
            return Files.readAllBytes(stored.toPath());
        } else {
            LOGGER.info("No cache available, reading EXIF data.");
            String data = readExifMetadata(file);
            Files.write(stored.toPath(), data.getBytes());
            return data.getBytes();
        }
    }

    /**
     * TODO.
     * @param file
     * @return
     */
    private File getExifStoredMetadata(final File file) {
        String galleryPath = new File(System.getProperty(GALLERY_PATH)).getAbsolutePath();
        String metadataPath = galleryPath + File.separator + "GALLERY_DATA/exif";
        return new File(file.getAbsolutePath().replace(galleryPath, metadataPath));
    }

    /**
     *
     * TODO.
     * @param file
     * @return
     * @throws IOException
     * @throws ImageProcessingException
     */
    public String readExifMetadata(final File file) throws IOException, ImageProcessingException {
        InputStream imageFile = new FileInputStream(file);
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        LOGGER.info("Read file {} and looking for EXIF data.", file.getAbsolutePath());
        ExifData data = new ExifData(file, metadata);
        return new ObjectMapper().writeValueAsString(data);
    }

}
