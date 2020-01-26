package fr.redby.gallery.servicepictures.bean;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * TODO: add lombok
 * TODO: move logic from constructor to service.
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
public class Picture {

    private String category;
    private String album;
    private File file;
    private Long size;
    private Integer width, height;

    public Picture(String category, String album, File f) {
        this.album = album;
        this.category = category;
        this.file = f;
        this.size = f.length();

        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
            this.height = image.getHeight();
            this.width = image.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
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

    public Long getSize() {
        return size;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
