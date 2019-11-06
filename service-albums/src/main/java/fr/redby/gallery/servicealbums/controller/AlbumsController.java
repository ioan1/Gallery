package fr.redby.gallery.servicealbums.controller;

import fr.redby.gallery.servicealbums.bean.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

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

    private static final Logger LOGGER = LoggerFactory.getLogger( AlbumsController.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";

    /**
     * @return Service retrieving all albums (folder names) and returns them as-is.
     */
    @RequestMapping(value = "/albums/{category}", method = RequestMethod.GET)
    public List<Album> getAlbums(final @PathVariable String category) {
        LOGGER.info("Getting albums for the category {}", category);
        File directory = new File(System.getProperty(GALLERY_PATH) + File.separator + category);
        LOGGER.info("Parsing {}.", directory.getAbsolutePath());
        if (directory.isDirectory()) {
            return Arrays.stream(directory.listFiles(f -> f.isDirectory()))
                    .peek(f -> LOGGER.debug(f.getAbsolutePath()))
                    .map(f -> new Album(category, f))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

}
