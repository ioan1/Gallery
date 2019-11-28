package fr.redby.gallery.service.albums.service;

import fr.redby.gallery.service.albums.bean.Album;
import fr.redby.gallery.service.albums.repository.AlbumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private static final Logger LOGGER = LoggerFactory.getLogger( AlbumService.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private AlbumRepository repository;

    /**
     * Gets all albums for a given category.
     * @param category the category
     * @return list of albums matching the selected criteria.
     */
    public List<Album> listAlbums(final @PathVariable String category) {
        LOGGER.info("Getting albums for the category {}", category);
        File directory = new File(System.getProperty(GALLERY_PATH) + File.separator + category);

        List<Album> cached = repository.findByCategory(category);
        if (cached != null && !cached.isEmpty()) {
            LOGGER.info("Returning {} albums cached from the database.", cached.size());
            return cached;
        } else {
            LOGGER.info("Parsing {} and saving the albums to the database.", directory.getAbsolutePath());
            if (directory.isDirectory()) {
                List<Album> res = Arrays.stream(directory.listFiles(f -> f.isDirectory()))
                        .peek(f -> LOGGER.debug(f.getAbsolutePath())).map(f -> new Album(category, f))
                        .collect(Collectors.toList());
                repository.saveAll(res);
                return res;
            } else {
                LOGGER.error("The provided path is not a folder.");
                return new ArrayList<>();
            }
        }
    }

    /**
     * Search for albums using a keyword on the name as well as the date.
     * @param keyword the keyword to use
     * @return a search result
     */
    public List<Album> searchAlbums(final String keyword) {
        LOGGER.info("Searching for albums with keyword {}", keyword);
        List<Album> res =
                repository.findAll()
                        .stream()
                        .filter(f -> searchAlbums(keyword, f))
                        .sorted()
                        .collect(Collectors.toList());
        LOGGER.info("Found {} albums matching the search criteria.", res.size());
        return res;
    }

    /**
     * Checks if a given album is matching the search criteria.
     * Checks on name and date.
     * @param keyword the keyword
     * @param album the album
     * @return boolean (matching or not).
     */
    private boolean searchAlbums(final String keyword, final Album album) {
        if (album.getName() == null)
            return false;
        boolean nameCheck = album
                .getName()
                .toLowerCase()
                .contains(keyword.toLowerCase());
        boolean dateCheck = album.getDate() != null && simpleDateFormat.format(album.getDate()).contains(keyword);
        return nameCheck || dateCheck;
    }

    /**
     * Re-discover all albums and collect basic statistics.
     * Stores the result in the DB.
     * @return the albums discovered by category
     */
    public Map<String, List<Album>> discoverAlbums() {
        Map<String, List<Album>> res = new HashMap<>();

        LOGGER.info("Discovering all albums.");
        repository.deleteAll();
        File directory = new File(System.getProperty(GALLERY_PATH));
        for (File category : directory.listFiles(f->isNumeric(f.getName()))) {
            res.put(category.getName(), listAlbums(category.getName()));
        }

        return res;
    }

    /**
     * Utility method to stipulate if the provided input is a valid integer or not.
     *
     * @param strNum input value
     * @return true or false whether it's an integer or not.
     */
    private boolean isNumeric(String strNum) {
        try {
            Integer i = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }
}
