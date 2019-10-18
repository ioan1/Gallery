
package fr.redby.gallery.servicealbums.bean;

/**
 * @author Ioan Bernevig
 * @version $Revision$
 */
public class Album {

    private String category;
    private String name;
    private String path;

    public Album(final String category, final String name, String absolutePath) {
        this.category = category;
        this.name = name;
        this.path = absolutePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
