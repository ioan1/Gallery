package fr.redby.gallery.servicepictures.controller;

import fr.redby.gallery.servicepictures.bean.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * Service dedicated to the management of pictures.
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController public class PicturesController {

    private static final Logger LOGGER = LoggerFactory.getLogger( PicturesController.class );
    public static final String GALLERY_PATH = "GALLERY_PATH";

    @RequestMapping(value = "/pictures/{category}/{album}", method = RequestMethod.GET)
    public List<Picture> listPictures(final @PathVariable String category, final @PathVariable String album)
            throws IOException {
        File directory = new File(System.getProperty(GALLERY_PATH));
        LOGGER.info("gallery directory: {}", directory);
        File folder = new File(directory, category + File.separator + album);
        LOGGER.info("Listing all pictures located in {}, exists={}.", folder.getAbsolutePath(), folder.exists());

        if (folder.isDirectory()) {
            return Files.walk(folder.toPath())
                    .map(p -> p.toFile())
                    .filter(f -> f.isFile())
                    .filter(f->f.getName().toLowerCase().endsWith(".jpg"))
                    .peek(f -> LOGGER.debug(f.getAbsolutePath()))
                    .map(f -> new Picture(category, album, f))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @RequestMapping(value = "/picture/full/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getFull(final HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String file = URLDecoder.decode(requestURL.split("/picture/full/")[1], "UTF-8");

        File picture = new File(file);
        LOGGER.info("Reading the file {}, exists={}", picture.getAbsolutePath(), picture.exists());
        return Files.readAllBytes(Paths.get(picture.getAbsolutePath()));
    }

    @RequestMapping(value = "/picture/small/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getSmall(final HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String file = URLDecoder.decode(requestURL.split("/picture/small/")[1], "UTF-8");

        File picture = new File(file);
        LOGGER.info("Reading the file {}, exists={}.", picture.getAbsolutePath(), picture.exists());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(picture)
                .size(300, 300)
                .crop(Positions.CENTER)
                .keepAspectRatio(true)
                .outputFormat("jpg")
                .toOutputStream(out);

        return out.toByteArray();
    }

}
