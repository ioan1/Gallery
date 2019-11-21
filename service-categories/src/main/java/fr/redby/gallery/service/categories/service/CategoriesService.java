
package fr.redby.gallery.service.categories.service;

import fr.redby.gallery.service.categories.bean.Category;
import fr.redby.gallery.service.categories.repository.CategoriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @RequestMapping(method = RequestMethod.GET)
    public List<Category> getCategories() {
        List<Category> cached = repository.findAll();
        if (cached != null && !cached.isEmpty()) {
            LOGGER.info("Return {} categories from database.", cached.size());
            return cached;
        } else {
            File directory = new File(System.getProperty(GALLERY_PATH));
            if (directory.isDirectory()) {
                cached = Arrays.stream(directory.listFiles(f -> f.isDirectory()))
                        .map(f -> new Category(f))
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
     * TODO.
     *
     * @param strNum
     * @return
     */
    private boolean isNumeric(String strNum) {
        try {
            Integer i = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
