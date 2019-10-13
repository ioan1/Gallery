package fr.redby.gallery.service.categories.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service dedicated to the management of categories.
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController
@RequestMapping ("/categories")
public class CategoriesController {

    public static final String GALLERY_PATH = "GALLERY_PATH";

    /**
     * @return Service retrieving all categories (folder names) and returns them as-is.
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<String> getCategories() {
        File directory = new File(System.getProperty(GALLERY_PATH));
        if (directory.isDirectory()) {
            return Arrays.stream(directory.listFiles(f -> f.isDirectory()))
                    .map(f -> f.getName())
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}
