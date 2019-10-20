package fr.redby.gallery.servicepictures.bean;

import java.io.File;

/**
 * @author Ioan Bernevig
 * @version $Revision$
 */
public class Picture {

    private String category;
    private String album;
    private File file;

    public Picture(String category, String album, File f) {
        this.album = album;
        this.category = category;
        this.file = f;
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

}
