package fr.redby.gallery.servicepictures.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import fr.redby.gallery.servicepictures.bean.ExifData;
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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service dedicated to the management of pictures.
 */
@Service
public class ExifService {

    private static final Logger LOGGER = LoggerFactory.getLogger( ExifService.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";

    @Autowired
    private ExifDataRepository exifDataRepository;

    /**
     * Reads the EXIF either from cache or from JPEG file and returns it in a byte array.
     * @param file JPEG file
     * @return byte array (JSON formatted)
     * @throws IOException any error is returned to the calling method.
     * @throws ImageProcessingException any error is returned to the calling method.
     */
    public ExifData getExifData(final File file) throws IOException, ImageProcessingException {
        LOGGER.info("getExifData({})", file.getAbsolutePath());
        Optional<ExifData> stored = exifDataRepository.findById(file.getAbsolutePath());
        if (stored.isPresent()) {
            LOGGER.info("The file exists, reading all bytes from the database.");
            return stored.get();
        } else {
            LOGGER.info("No cache available, reading EXIF data.");
            ExifData data = readExifMetadata(file);
            LOGGER.info("Going to save in the database");
            exifDataRepository.save(data);
            LOGGER.info("Saved new entity");
            return data;
        }
    }

    /**
     * Reads the Exif data from the provided JPEG file and returns it in a pretty-formatted string.
     * @param file JPEG file to read
     * @return string holding the EXIF information in JSON format
     * @throws IOException any error is returned to the calling method.
     * @throws ImageProcessingException any error is returned to the calling method.
     */
    public ExifData readExifMetadata(final File file) throws IOException, ImageProcessingException {
        InputStream imageFile = new FileInputStream(file);
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        LOGGER.info("Read file {} and looking for EXIF data.", file.getAbsolutePath());
        return new ExifData(file, metadata);
    }

    /**
     * Checks the exif information on all pictures in a category.
     * @param category the category/year
     * @return the number of pictures discovered
     */
    public Long discoverCategory(final String category) {
        File folder = new File(new File(System.getProperty(GALLERY_PATH)), category);
        LOGGER.info("Discovering EXIF information on all pictures located in {}, exists={}.", folder.getAbsolutePath(), folder.exists());
        try (Stream<Path> stream = Files.walk(folder.toPath())) {
            return stream
                    .map(p -> p.toFile())
                    .filter(f -> f.isFile())
                    .filter(f->f.getName().toLowerCase().endsWith(".jpg"))
                    .peek(f -> LOGGER.debug("Triggering the discovering of Exif data for {}.", f.getAbsolutePath()))
                    .peek(f -> {
                        try {
                            getExifData(f);
                        } catch (IOException | ImageProcessingException e) {
                            LOGGER.error("Error when getting Exif data for {}.", f);
                        }
                    })
                    .count();
        } catch (IOException e) {
            LOGGER.error("Error when discovering categories.", e);
            return null;
        }
    }

}
