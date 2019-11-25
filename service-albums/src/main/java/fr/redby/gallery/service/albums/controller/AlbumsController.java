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
import java.util.Map;
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
     * Discovers all existing albums and basic statistics over them.
     * Meant to be called at regular interval by the scheduler service.
     * This will be cached into the database allowing the search by example.
     * @return result of the discovery.
     */
    @RequestMapping(value = "/albums/discover", method = RequestMethod.GET)
    public Map<String, List<Album>> discoverAlbums() {
        return service.discoverAlbums();
    }

    /**
     * @return Service retrieving all albums (folder names) and returns them as-is.
     */
    @RequestMapping(value = "/albums/list/{category}", method = RequestMethod.GET)
    public List<Album> listAlbums(final @PathVariable String category) {
        return service.listAlbums(category);
    }

    /**
     * @return Service searching for all albums matching the keyword.
     */
    @RequestMapping(value = "/albums/search/{keyword}", method = RequestMethod.GET)
    public List<Album> searchAlbums(final @PathVariable String keyword) {
        return service.searchAlbums(keyword);
    }

}
