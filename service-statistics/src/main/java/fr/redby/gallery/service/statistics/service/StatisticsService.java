package fr.redby.gallery.service.statistics.service;

import fr.redby.gallery.service.statistics.beans.DataType;
import fr.redby.gallery.service.statistics.beans.DiskUsage;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import fr.redby.gallery.service.statistics.repositories.SizePerYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger( StatisticsService.class );

    private static final long GB = 1024*1024*1024;
    private static final String WAIT_FOR_PROCESSING = "A_TRIER";

    @Autowired
    private SizePerYearRepository sizePerYearRepository;

    public DiskUsage getDiskUsage() {
        File root = new File(System.getProperty("GALLERY_PATH"));
        LOGGER.info("Total space in {}: {}", System.getProperty("GALLERY_PATH"), root.getTotalSpace());
        LOGGER.info("Free space in {}: {}", System.getProperty("GALLERY_PATH"), root.getFreeSpace());
        LOGGER.info("Usable space in {}: {}", System.getProperty("GALLERY_PATH"), root.getUsableSpace());

        int used = (int) (root.getTotalSpace()/GB - root.getFreeSpace()/GB);
        int available = (int) (root.getTotalSpace() / GB);

        return new DiskUsage(used, available);
    }

    public SizePerYear getSizePerYear() throws IOException {
        Optional<SizePerYear> existing = sizePerYearRepository.findById(SizePerYearRepository.STAT_IDENTIFIER);
        if (existing.isPresent()) {
            LOGGER.info("Returning the size per year from the database.");
            return existing.get();
        } else {
            SizePerYear result = new SizePerYear();
            File root = new File(System.getProperty("GALLERY_PATH"));
            for (File folder : root.listFiles(f -> f.isDirectory())) {
                if (!folder.getName().matches("[0-9]+")) continue;
                int folderSize = (int) (Files.walk(folder.toPath())
                        .filter(p -> p.toFile().isFile())
                        .mapToLong(p -> p.toFile().length())
                        .sum() / GB);
                int year = Integer.parseInt(folder.getName());
                LOGGER.info("{} => {}", year, folderSize);
                result.getData().add(new DataType(year, folderSize));
            }
            Collections.sort(result.getData());
            LOGGER.info("Saving the size per year into the database.");
            sizePerYearRepository.save(result);
            return result;
        }
    }

    public Long getWaitForProcessing() throws IOException {
        File root = new File(System.getProperty("GALLERY_PATH") + File.separator + WAIT_FOR_PROCESSING);
        return Files.walk(root.toPath())
                .filter(p -> p.toFile().isFile())
                .count();
    }
}
