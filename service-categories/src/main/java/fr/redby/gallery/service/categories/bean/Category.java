package fr.redby.gallery.service.categories.bean;

import org.springframework.data.annotation.Id;

import java.io.File;
import java.io.Serializable;

public class Category implements Serializable, Comparable {
    @Id
    private String name;
    private String path;

    public Category() {
     super();
    }

    public Category(final File file) {
        this.name = file.getName();
        this.path = file.getAbsolutePath();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int compareTo(final Object o) {
        return this.name.compareTo(((Category)o).name);
    }
}
