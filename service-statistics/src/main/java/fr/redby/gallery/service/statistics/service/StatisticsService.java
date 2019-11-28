package fr.redby.gallery.service.statistics.service;

import fr.redby.gallery.service.statistics.beans.*;
import fr.redby.gallery.service.statistics.repositories.PicturesPerYearRepository;
import fr.redby.gallery.service.statistics.repositories.SizePerYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service dedicated to any statistical processing.
 */
@Service public class StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsService.class);

    private static final long GB = 1024l * 1024l * 1024l;
    private static final String WAIT_FOR_PROCESSING = "A_TRIER";
    private static final String REDIS = "redis";

    @Autowired private SizePerYearRepository sizePerYearRepository;

    @Autowired private PicturesPerYearRepository picturesPerYearRepository;

    /**
     * Returns the disk usage (free space vs used).
     *
     * @return DiskUsage entity
     */
    public DiskUsage getDiskUsage() {
        File root = new File(System.getProperty("GALLERY_PATH"));
        LOGGER.info("Total space in {}: {}", System.getProperty("GALLERY_PATH"), root.getTotalSpace());
        LOGGER.info("Free space in {}: {}", System.getProperty("GALLERY_PATH"), root.getFreeSpace());
        LOGGER.info("Usable space in {}: {}", System.getProperty("GALLERY_PATH"), root.getUsableSpace());

        int used = (int) (root.getTotalSpace() / GB - root.getFreeSpace() / GB);
        int available = (int) (root.getTotalSpace() / GB);

        return new DiskUsage(used, available);
    }

    /**
     * Returns the sizes of each category per year.
     *
     * @return Everything is wrapped into a SizePerYear entity.
     * @throws IOException any error is returned to the calling method.
     */
    public SizePerYear getSizePerYear() throws IOException {
        Optional<SizePerYear> existing = sizePerYearRepository.findById(SizePerYearRepository.STAT_IDENTIFIER);
        if (existing.isPresent()) {
            LOGGER.info("Returning the size per year from the database.");
            return existing.get();
        } else {
            SizePerYear result = new SizePerYear();
            File root = new File(System.getProperty("GALLERY_PATH"));
            for (File folder : root.listFiles(f -> f.isDirectory())) {
                if (!folder.getName().matches("[0-9]+"))
                    continue;
                try (Stream<Path> stream = Files.walk(folder.toPath())) {
                    int folderSize =
                            (int) (stream.filter(p -> p.toFile().isFile()).mapToLong(p -> p.toFile().length()).sum()
                                    / GB);
                    int year = Integer.parseInt(folder.getName());
                    LOGGER.info("{} => {}", year, folderSize);
                    result.getData().add(new DataType(year, folderSize));
                }
            }
            Collections.sort(result.getData());
            LOGGER.info("Saving the size per year into the database.");
            sizePerYearRepository.save(result);
            return result;
        }
    }

    /**
     * @return the number of images waiting to be processed.
     * @throws IOException Any error is returned to the calling method.
     */
    public Long getWaitForProcessing() throws IOException {
        File root = new File(System.getProperty("GALLERY_PATH") + File.separator + WAIT_FOR_PROCESSING);
        return Files.walk(root.toPath()).filter(p -> p.toFile().isFile()).count();
    }

    /**
     * Return the pictures per year statistics wrapped within a PicturesPerYearWrapper.
     *
     * @return PicturesPerYearWrapper
     */
    public PicturesPerYearWrapper getPicturesPerYear() {
        List<PicturesPerYear> all = picturesPerYearRepository.findAll();
        Collections.sort(all);
        return new PicturesPerYearWrapper(all);
    }

    /**
     * @return the number of thumbnails that are cached in the redis database.
     */
    public Long getCachedThumbnails() {
        try (Jedis db = new Jedis(REDIS)) {
            return db.dbSize();
        }
    }
}
