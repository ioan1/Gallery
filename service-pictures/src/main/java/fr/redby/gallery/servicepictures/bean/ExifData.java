package fr.redby.gallery.servicepictures.bean;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class ExifData implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger( ExifData.class );

    @Id
    private String id;
    private String cameraMake, cameraModel;
    private Date dateDiscovery, dateTaken;

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
        this.dateTaken = getImageCreationDate(metadata);
        this.cameraMake = getStringValue(metadata, ExifSubIFDDirectory.TAG_MAKE);
        this.cameraModel = getStringValue(metadata, ExifSubIFDDirectory.TAG_MODEL);
        this.directories = new HashMap<String, List<ExifTag>>();
        for (Directory directory : metadata.getDirectories()) {
            List<ExifTag> tags = new ArrayList<>();
            for (Tag tag : directory.getTags())  {
                tags.add(new ExifTag(tag));
            }
            this.directories.put(directory.getName(), tags);
        }
        LOGGER.info("Created ExifData object holding {} EXIF directories.", this.directories.size());
    }

    /**
     * TODO.
     * @param metadata
     * @return
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
     * TODO.
     * @param metadata
     * @return
     */
    private Date getImageCreationDate(final Metadata metadata) {
        for (Directory dir : metadata.getDirectories()) {
            Date d = dir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            if (d != null && d.getYear() < 1900) { // Needed as some DSLRs return years on 2 digits ... .
                return d;
            }
        }
        return null;
    }

    public String getId() { return id; }

    public HashMap<String, List<ExifTag>> getDirectories() {
        return directories;
    }

    public Date getDateDiscovery() {
        return dateDiscovery;
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

    public void setDirectories(HashMap<String, List<ExifTag>> directories) {
        this.directories = directories;
    }
}
