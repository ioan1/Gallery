package fr.redby.gallery.servicepictures.controller;

import fr.redby.gallery.servicepictures.bean.Picture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service dedicated to the management of pictures.
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController public class PicturesController {

    public static final String GALLERY_PATH = "GALLERY_PATH";

    @RequestMapping(value = "/pictures/{category}/{album}", method = RequestMethod.GET)
    public List<Picture> listPictures(final @PathVariable String category, final @PathVariable String album) {
        File directory = new File(System.getProperty(GALLERY_PATH));
        System.out.println("gallery directory: " + directory);
        File folder = new File(directory, category + File.separator + album);
        System.out.println("Listing all pictures located in " + folder.getAbsolutePath() + " , exists="+folder.exists());

        if (folder.isDirectory()) {
            return Arrays.stream(folder.listFiles(f -> f.isFile()))
                    .filter(f->f.getName().toLowerCase().endsWith(".jpg"))
                    .peek(f -> System.out.println(f.getAbsolutePath()))
                    .map(f -> new Picture(category, album, f))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "/picture/full/{file}", method = RequestMethod.GET)
    public byte[] getFull(final @PathVariable String file) throws IOException {
        File directory = new File(System.getProperty(GALLERY_PATH));
        return Files.readAllBytes(Paths.get(directory.getAbsolutePath() + File.separator + file));
    }

}
