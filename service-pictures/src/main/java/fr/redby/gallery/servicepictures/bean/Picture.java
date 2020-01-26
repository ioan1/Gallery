package fr.redby.gallery.servicepictures.bean;

import java.io.File;
import java.util.Optional;

/**
 *
 * TODO: add lombok
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
public class Picture {

    private String category;
    private String album;
    private File file;
    private ExifData exifData;

    public Picture(String category, String album, File f, Optional<ExifData> ed) {
        this.album = album;
        this.category = category;
        this.file = f;
        if (ed.isPresent()) {
            this.exifData = ed.get();
        }
    }

    public String getCategory() {
        return category;
    }

    public String getAlbum() {
        return album;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return file.getName();
    }

    public String getPath() {
        return file.getAbsolutePath();
    }

    public ExifData getExifData() { return exifData; }
}
