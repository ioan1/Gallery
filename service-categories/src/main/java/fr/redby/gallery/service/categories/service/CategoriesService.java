
package fr.redby.gallery.service.categories.service;

import fr.redby.gallery.service.categories.bean.Category;
import fr.redby.gallery.service.categories.repository.CategoriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesService {

    private static final Logger LOGGER = LoggerFactory.getLogger( CategoriesService.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";

    @Autowired
    private CategoriesRepository repository;

    /**
     * @return Service retrieving all categories (folder names) and returns them as-is.
     */
    public List<Category> getCategories() {
        List<Category> cached = repository.findAll();
        if (cached.isEmpty()) {
            LOGGER.info("Return {} categories from database.", cached.size());
            return cached;
        } else {
            File directory = new File(System.getProperty(GALLERY_PATH));
            if (directory.isDirectory()) {
                cached = Arrays.stream(directory.listFiles(File::isDirectory))
                        .map(Category::new)
                        .filter(f -> isNumeric(f.getName()))
                        .sorted((o1, o2) -> o2.compareTo(o1))
                        .collect(Collectors.toList());
                repository.saveAll(cached);
                LOGGER.info("Return {} categories read from disk and cached into the database.", cached.size());
                return cached;
            } else {
                LOGGER.error("The provided GALLERY_PATH is not a folder.");
                return new ArrayList<>();
            }
        }
    }

    /**
     * Utility method to stipulate if the provided input is a valid integer or not.
     *
     * @param strNum input value
     * @return true or false whether it's an integer or not.
     */
    private boolean isNumeric(String strNum) {
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
