package fr.redby.gallery.service.albums.controller;

import fr.redby.gallery.service.albums.bean.Album;
import fr.redby.gallery.service.albums.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Service dedicated to the management of albums.
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController
public class AlbumsController {

    private final AlbumService service;

    @Autowired
    public AlbumsController(AlbumService service) {
        this.service = service;
    }

    /**
     * Discovers all existing albums and basic statistics over them.
     * Meant to be called at regular interval by the scheduler service.
     * This will be cached into the database allowing the search by example.
     * @return result of the discovery.
     */
    @GetMapping("/albums/discover")
    public Map<String, List<Album>> discoverAlbums() {
        return service.discoverAlbums();
    }

    /**
     * @return Service retrieving all albums (folder names) and returns them as-is.
     */
    @GetMapping("/albums/list/{category}")
    public List<Album> listAlbums(final @PathVariable String category) {
        return service.listAlbums(category);
    }

    /**
     * @return Service searching for all albums matching the keyword.
     */
    @GetMapping("/albums/search/{keyword}")
    public List<Album> searchAlbums(final @PathVariable String keyword) {
        return service.searchAlbums(keyword);
    }

}
