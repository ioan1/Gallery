package fr.redby.gallery.service.albums.service;

import fr.redby.gallery.service.albums.bean.Album;
import fr.redby.gallery.service.albums.repository.AlbumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private static final Logger LOGGER = LoggerFactory.getLogger( AlbumService.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";

    @Autowired
    private AlbumRepository repository;

    /**
     * TODO.
     * @param category
     * @return
     */
    public List<Album> listAlbums(final @PathVariable String category) {
        LOGGER.info("Getting albums for the category {}", category);
        File directory = new File(System.getProperty(GALLERY_PATH) + File.separator + category);

        List<Album> cached = repository.findAll();
        if (cached != null && !cached.isEmpty()) {
            LOGGER.info("Returning {} albums cached from the database.", cached.size());
            return cached;
        } else {
            LOGGER.info("Parsing {} and saving the albums to the database.", directory.getAbsolutePath());
            if (directory.isDirectory()) {
                return Arrays.stream(directory.listFiles(f -> f.isDirectory()))
                        .peek(f -> LOGGER.debug(f.getAbsolutePath()))
                        .map(f -> new Album(category, f))
                        .collect(Collectors.toList());
            } else {
                LOGGER.error("The provided path is not a folder.");
                return new ArrayList<>();
            }
        }
    }

    /**
     * TODO.
     * @param keyword
     * @return
     */
    public List<Album> searchAlbums(final String keyword) {
        LOGGER.info("Searching for albums with keyword {}", keyword);

        return new ArrayList<>();
    }

}
