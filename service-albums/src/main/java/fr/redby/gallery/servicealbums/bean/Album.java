
package fr.redby.gallery.servicealbums.bean;

/**
 * @author Ioan Bernevig
 * @version $Revision$
 */
public class Album implements Comparable<Album>{

    private String category;
    private String name;

    public Album(final String category, final String name) {
        this.category = category;
        this.name = name;
    }

    @Override public int compareTo(final Album o) {
        return this.name.compareTo(o.name);
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
