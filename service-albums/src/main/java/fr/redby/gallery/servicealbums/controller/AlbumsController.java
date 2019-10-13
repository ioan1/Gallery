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
@RequestMapping ("/albums")
public class AlbumsController {

    public static final String GALLERY_PATH = "GALLERY_PATH";

    /**
     * @return Service retrieving all albums (folder names) and returns them as-is.
     */
    @GetMapping("/{category}")
    @RequestMapping(method = RequestMethod.GET)
    public List<Album> getAlbums(final @PathVariable String category) {
        File directory = new File(System.getProperty(GALLERY_PATH) + File.pathSeparator + category);
        if (directory.isDirectory()) {
            return Arrays.stream(directory.listFiles(f -> f.isDirectory()))
                    .map(f -> new Album(category, f.getName()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}
