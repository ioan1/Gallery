package fr.redby.gallery.service.statistics.controller;

import fr.redby.gallery.service.statistics.beans.DiskUsage;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import fr.redby.gallery.service.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    private StatisticsService service;

    @RequestMapping(value = "disk", method = RequestMethod.GET)
    public DiskUsage getDiskUsage() {
        return service.getDiskUsage();
    }

    @RequestMapping(value = "waitForProcessing", method = RequestMethod.GET)
    public Long getWaitForProcessing() throws IOException {
        return service.getWaitForProcessing();
    }

    @RequestMapping (value = "sizePerYear", method = RequestMethod.GET)
    public SizePerYear getFilesPerYear() throws IOException {
        return service.getSizePerYear();
    }

}
