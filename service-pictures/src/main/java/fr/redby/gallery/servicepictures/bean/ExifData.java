package fr.redby.gallery.servicepictures.bean;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
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
    private Date date;

    private HashMap<String, List<ExifTag>> directories;

    public ExifData() {
        super();
        LOGGER.debug("Default constructor.");
    }

    public ExifData(final File picture, final Metadata metadata) {
        this.id = picture.getAbsolutePath();
        this.date = new Date();
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

    public String getId() { return id; }

    public HashMap<String, List<ExifTag>> getDirectories() {
        return directories;
    }

    public Date getDate() {
        return date;
    }
}
