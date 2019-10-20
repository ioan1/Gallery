package fr.redby.gallery.servicealbums.controller;

import fr.redby.gallery.servicealbums.bean.Album;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service dedicated to the management of albums.
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController
public class AlbumsController {

    public static final String GALLERY_PATH = "GALLERY_PATH";

    /**
     * @return Service retrieving all albums (folder names) and returns them as-is.
     */
    @RequestMapping(value = "/albums/{category}", method = RequestMethod.GET)
    public List<Album> getAlbums(final @PathVariable String category) {
        System.out.println("getting albums for the category " + category);
        File directory = new File(System.getProperty(GALLERY_PATH) + File.separator + category);
        System.out.println("Parsing "+directory.getAbsolutePath());
        if (directory.isDirectory()) {
            return Arrays.stream(directory.listFiles(f -> f.isDirectory()))
                    .peek(f -> System.out.println(f.getAbsolutePath()))
                    .map(f -> new Album(category, f))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}
