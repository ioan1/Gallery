package fr.redby.gallery.service.statistics.controller;

import fr.redby.gallery.service.statistics.beans.DiskUsage;
import fr.redby.gallery.service.statistics.beans.PicturesPerYearWrapper;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import fr.redby.gallery.service.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Service dedicated to the extraction of statistics.
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController
@RequestMapping ("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService service;

    @GetMapping("disk")
    public DiskUsage getDiskUsage() {
        return service.getDiskUsage();
    }

    /**
     * Returns the number of images waiting to be processed.
     * @return number of images waiting to be processed.
     * @throws IOException any error is returned to the calling method.
     */
    @GetMapping("waitForProcessing")
    public Long getWaitForProcessing() throws IOException {
        return service.getWaitForProcessing();
    }

    /**
     * Returns the number of thumbnails caches in the Redis database.
     * @return number of thumbnails cached in the Redis database.
     * @throws IOException any error is returned to the calling method.
     */
    @GetMapping("cachedThumbnails")
    public Long getCachedThumbnails() throws IOException {
        return service.getCachedThumbnails();
    }

    /**
     * Returns a map (wrapped in a {@link SizePerYear} object providing the total size
     * in GB per year/category. To be used on the dashboard by example.
     * @return SizePerYear
     * @throws IOException any error is returned to the calling method.
     */
    @GetMapping ("sizePerYear")
    public SizePerYear getFilesPerYear() throws IOException {
        return service.getSizePerYear();
    }

    /**
     * Returns a map (wrapped in a {@link PicturesPerYearWrapper} object providing the number of pictures
     * per year/category. To be used on the dashboard by example.
     * @return PicturesPerYearWrapper
     * @throws IOException any error is returned to the calling method.
     */
    @GetMapping ("picturesPerYear")
    public PicturesPerYearWrapper getPicturesPerYear() throws IOException {
        return service.getPicturesPerYear();
    }

}
