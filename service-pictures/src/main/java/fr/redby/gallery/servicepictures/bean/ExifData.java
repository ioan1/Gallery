package fr.redby.gallery.servicepictures.bean;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Class wrapping the Exif data of a picture.
 * Extracted from all JPEG files.
 */
@SuppressWarnings("squid:S1068")
public class ExifData implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger( ExifData.class );

    @Id
    private String id;
    private String cameraMake, cameraModel;
    private Date dateDiscovery, dateTaken;
    private Integer width, height;
    private Long size;

    private HashMap<String, List<ExifTag>> directories;

    public ExifData() {
        super();
        LOGGER.debug("Default constructor.");
    }

    /**
     * Copy/converter constructor taking as parameter a Metadata object and returns an ExifData (more useful for the gallery).
     * @param picture the file to read
     * @param metadata raw metadata
     */
    public ExifData(final File picture, final Metadata metadata) {
        this.id = picture.getAbsolutePath();
        this.dateDiscovery = new Date();
        this.size = picture.length();
        this.dateTaken = getImageCreationDate(metadata);
        this.cameraMake = getStringValue(metadata, ExifSubIFDDirectory.TAG_MAKE);
        this.cameraModel = getStringValue(metadata, ExifSubIFDDirectory.TAG_MODEL);
        this.directories = new HashMap<>();
        for (Directory directory : metadata.getDirectories()) {
            List<ExifTag> tags = new ArrayList<>();
            for (Tag tag : directory.getTags())  {
                tags.add(new ExifTag(tag));
            }
            this.directories.put(directory.getName(), tags);
        }

        try {
            BufferedImage image = ImageIO.read(picture);
            this.height = image.getHeight();
            this.width = image.getWidth();
        } catch (IOException e) {
            LOGGER.error("Cannot read image width and height from {}.", picture);
        }
        LOGGER.info("Created ExifData object holding {} EXIF directories.", this.directories.size());
    }

    /**
     * Returns a textual value from the metadata. Searches in all Exif directories.
     * If none found, returns null.
     * @param metadata the metadata directory
     * @param tag the tag to be found
     * @return the string representation
     */
    private String getStringValue(final Metadata metadata, final int tag) {
        for (Directory dir : metadata.getDirectories()) {
            String value = dir.getString(tag);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Returns the image creation date from the Exif directory.
     * @param metadata the Exif directories
     * @return the image creation date
     */
    private Date getImageCreationDate(final Metadata metadata) {
        for (Directory dir : metadata.getDirectories()) {
            Date d = dir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            if (d != null && d.getYear() > 0) { // Needed as some DSLRs return years on 2 digits ... .
                return d;
            }
        }
        return null;
    }

    public String getCameraMake() {
        return cameraMake;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Long getSize() {
        return size;
    }

    public Date getDateDiscovery() {
        return dateDiscovery;
    }
}
