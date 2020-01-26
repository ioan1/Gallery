package fr.redby.gallery.servicepictures.controller;

import fr.redby.gallery.servicepictures.bean.Picture;
import fr.redby.gallery.servicepictures.service.PicturesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * Service dedicated to the management of pictures.
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController public class PicturesController {

    private static final Logger LOGGER = LoggerFactory.getLogger( PicturesController.class );

    @Autowired
    private PicturesService service;

    @RequestMapping(value = "/pictures/thumbnailsCache", method = RequestMethod.DELETE)
    public Boolean deleteThumbnailsCache() {
        service.deleteThumbnailsCache();
        return true;
    }

    @RequestMapping(value = "/pictures/{category}/{album}", method = RequestMethod.GET)
    public List<Picture> listPictures(final @PathVariable String category, final @PathVariable String album) {
        return service.listPictures(category, album);
    }

    @RequestMapping(value = "/picture/medium/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getMedium(final HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String file = URLDecoder.decode(requestURL.split("/picture/medium/")[1], "UTF-8");

        File picture = new File(file);
        LOGGER.info("Reading the medium sized file {}, exists={}", picture.getAbsolutePath(), picture.exists());
        return service.getMedium(new File(file));
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
        return service.getSmall(new File(file), 125);
    }

    @RequestMapping(value = "/picture/micro/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getMicro(final HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String file = URLDecoder.decode(requestURL.split("/picture/micro/")[1], "UTF-8");
        return service.getSmall(new File(file), 75);
    }

}
