package fr.redby.gallery.servicepictures.bean;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import fr.redby.gallery.servicepictures.service.PicturesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExifData {

    private static final Logger LOGGER = LoggerFactory.getLogger( ExifData.class );

    private File picture;
    private Date date;
    private List<Directory> directories;

    public ExifData(final File picture, final Metadata metadata) {
        this.picture = picture;
        this.date = new Date();
        this.directories = new ArrayList<>();
        for (Directory directory : metadata.getDirectories()) {
            this.directories.add(directory);
        }
        LOGGER.info("Created ExifData object holding {} EXIF directories.", this.directories.size());
    }

    public File getPicture() {
        return picture;
    }

    public List<Directory> getDirectories() {
        return directories;
    }

    public Date getDate() {
        return date;
    }
}
