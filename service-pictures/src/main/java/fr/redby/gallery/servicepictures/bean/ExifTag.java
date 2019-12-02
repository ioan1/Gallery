package fr.redby.gallery.servicepictures.bean;

import com.drew.metadata.Tag;

import java.io.Serializable;

/**
 * Class wrapping an Exif tag. A tag is basically defined by a type, a name and a description.
 */
@SuppressWarnings("squid:S1068")
public class ExifTag implements Serializable {

    private int tagType;
    private String tagTypeHex;
    private String tagName;
    private String description;

    /**
     * Default constructor.
     */
    public ExifTag() {
        super();
    }

    /**
     * Copy constructor.
     * @param t Exif Tag object
     */
    public ExifTag(final Tag t) {
        this.description = t.getDescription();
        this.tagName = t.getTagName();
        this.tagType = t.getTagType();
        this.tagTypeHex = t.getTagTypeHex();
    }

}
