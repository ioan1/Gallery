
package fr.redby.gallery.servicealbums.bean;

/**
 * @author Ioan Bernevig
 * @version $Revision$
 */
public class Album {

    private String category;
    private String name;

    public Album(final String category, final String name) {
        this.category = category;
        this.name = name;
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
}
