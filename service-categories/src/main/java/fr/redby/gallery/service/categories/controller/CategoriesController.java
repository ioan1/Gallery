package fr.redby.gallery.service.categories.controller;

import fr.redby.gallery.service.categories.bean.Category;
import fr.redby.gallery.service.categories.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Service dedicated to the management of categories.
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController
public class CategoriesController {

    @Autowired
    private CategoriesService service;

    /**
     * @return Service retrieving all categories (folder names) and returns them as-is.
     */
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return service.getCategories();
    }

}
