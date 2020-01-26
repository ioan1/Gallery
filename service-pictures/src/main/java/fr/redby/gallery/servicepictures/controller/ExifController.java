package fr.redby.gallery.servicepictures.controller;

import com.drew.imaging.ImageProcessingException;
import fr.redby.gallery.servicepictures.bean.ExifData;
import fr.redby.gallery.servicepictures.bean.Picture;
import fr.redby.gallery.servicepictures.service.ExifService;
import fr.redby.gallery.servicepictures.service.PicturesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Service dedicated to the management of pictures.
 *
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController public class ExifController {

    private static final Logger LOGGER = LoggerFactory.getLogger( ExifController.class );

    @Autowired
    private ExifService service;

    @RequestMapping(value = "/picture/exif/**", method = RequestMethod.GET)
    public ExifData getExifData(final HttpServletRequest request) throws IOException, ImageProcessingException {
        String requestURL = request.getRequestURL().toString();
        String file = URLDecoder.decode(requestURL.split("/picture/exif/")[1], "UTF-8");
        return service.getExifData(new File(file));
    }

    @RequestMapping(value = "/exif/discover/{category}", method = RequestMethod.GET)
    public Long discoverCategory(final @PathVariable String category) throws IOException {
        return service.discoverCategory(category);
    }

    @RequestMapping(value = "/exif", method = RequestMethod.DELETE)
    public boolean delete() {
        service.delete();
        return true;
    }

}
