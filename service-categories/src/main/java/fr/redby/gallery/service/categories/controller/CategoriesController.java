package fr.redby.gallery.service.categories.controller;

import fr.redby.gallery.service.categories.bean.Category;
import fr.redby.gallery.service.categories.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
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
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService service;

    /**
     * @return Service retrieving all categories (folder names) and returns them as-is.
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Category> getCategories() {
        return service.getCategories();
    }

}
