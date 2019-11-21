package fr.redby.gallery.service.albums.controller;

import fr.redby.gallery.service.albums.bean.Album;
import fr.redby.gallery.service.albums.service.AlbumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AlbumService service;

    /**
     * @return Service retrieving all albums (folder names) and returns them as-is.
     */
    @RequestMapping(value = "/albums/{category}", method = RequestMethod.GET)
    public List<Album> getAlbums(final @PathVariable String category) {
        return service.getAlbums(category);
    }

}
