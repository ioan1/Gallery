package fr.redby.gallery.service.categories.bean;

import org.springframework.data.annotation.Id;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class wrapping a category (year).
 */
@SuppressWarnings("squid:S1068")
public class Category implements Serializable, Comparable {
    @Id
    private String name;
    private String path;

    /**
     * Default constructor.
     */
    public Category() {
     super();
    }

    /**
     * Copy constructor consider a folder.
     * @param file the folder of the category
     */
    public Category(final File file) {
        this.name = file.getName();
        this.path = file.getAbsolutePath();
    }

    /**
     * Returns the folder (category) name.
     * @return String
     */
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(final Object o) {
        return this.name.compareTo(((Category)o).name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name);
    }
}
