/**
 * Copyright European Commission's
 * Taxation and Customs Union Directorate-General (DG TAXUD).
 */
package fr.redby.gallery.service.albums.bean;

import java.io.File;

/**
 * Enumeration providing the list of files supported by the gallery.
 * @author FITSDEV3
 * @version $Revision$
 */
public enum TypeOfFile {
    /**
     * any kind of picture.
     */
    PICTURE ("jpg", "png"),
    /**
     * any kind of videos.
     */
    VIDEO ("avi"),
    /**
     * other kind of files, including raw pictures.
     */
    OTHER;

    private String[] extensions;
    private TypeOfFile(String ... extension) {
        this.extensions = extension;
    }

    public static TypeOfFile defineFor(File f) {
        final String normalizedName = f.getName().toLowerCase();
        for (TypeOfFile type : TypeOfFile.values()) {
            for (String extension : type.getExtensions()) {
                if (normalizedName.endsWith(extension))
                    return type;
            }
        }
        return OTHER;
    }

    public String[] getExtensions() {
        return extensions;
    }

}
