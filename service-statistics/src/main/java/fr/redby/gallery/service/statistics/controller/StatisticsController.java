package fr.redby.gallery.service.statistics.controller;

import fr.redby.gallery.service.statistics.beans.DiskUsage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service dedicated to the extraction of statistics.
 * @author Ioan Bernevig
 * @version $Revision$
 */
@RestController
@RequestMapping ("/statistics")
public class StatisticsController {

    public static final String GALLERY_PATH = System.getProperty("GALLERY_PATH");

    @RequestMapping(value = "disk", method = RequestMethod.GET)
    public DiskUsage getDiskUsage() {

        File root = new File(GALLERY_PATH);
        return new DiskUsage( ((int)root.getTotalSpace() - (int)root.getFreeSpace())/(1024*1024*1024), (int) (root.getTotalSpace() /(1024*1024*1024)));
    }

}
