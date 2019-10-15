package fr.redby.gallery.service.statistics.controller;

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

    public static final String GALLERY_PATH = "GALLERY_PATH";

    @RequestMapping(value = "years/number", method = RequestMethod.GET)
    public int getNumberYears() {
        return 9; // TODO
    }

}
