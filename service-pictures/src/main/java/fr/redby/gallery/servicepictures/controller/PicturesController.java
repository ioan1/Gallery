package fr.redby.gallery.servicepictures.controller;

import fr.redby.gallery.servicepictures.bean.Picture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import net.coobird.thumbnailator.Thumbnails;

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

    @RequestMapping(value = "/picture/full/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getFull(final HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String file = URLDecoder.decode(requestURL.split("/picture/full/")[1], "UTF-8");

        File picture = new File(new File(System.getProperty(GALLERY_PATH)), file);
        System.out.println("Reading the file " + picture.getAbsolutePath() + ", exists="+picture.exists());
        return Files.readAllBytes(Paths.get(picture.getAbsolutePath()));
    }

    @RequestMapping(value = "/picture/small/**", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getSmall(final HttpServletRequest request) throws IOException {
        String requestURL = request.getRequestURL().toString();
        String file = URLDecoder.decode(requestURL.split("/picture/small/")[1], "UTF-8");

        File picture = new File(new File(System.getProperty(GALLERY_PATH)), file);
        System.out.println("Reading the file " + picture.getAbsolutePath() + ", exists="+picture.exists());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(picture)
                .size(300, 300)
                .outputFormat("jpg")
                .toOutputStream(out);

        return out.toByteArray();
    }

}
