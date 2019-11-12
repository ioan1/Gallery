package fr.redby.gallery.servicepictures.bean;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExifData {

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
