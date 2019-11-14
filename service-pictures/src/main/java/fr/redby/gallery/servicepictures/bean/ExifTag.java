package fr.redby.gallery.servicepictures.bean;

import com.drew.metadata.Tag;

import java.io.Serializable;

public class ExifTag implements Serializable {

    private int tagType;
    private String tagTypeHex;
    private String tagName;
    private String description;

    public ExifTag() {
        super();
    }

    public ExifTag(final Tag t) {
        this.description = t.getDescription();
        this.tagName = t.getTagName();
        this.tagType = t.getTagType();
        this.tagTypeHex = t.getTagTypeHex();
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public void setTagTypeHex(String tagTypeHex) {
        this.tagTypeHex = tagTypeHex;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTagType() {
        return tagType;
    }

    public String getTagTypeHex() {
        return tagTypeHex;
    }

    public String getTagName() {
        return tagName;
    }

    public String getDescription() {
        return description;
    }

}
