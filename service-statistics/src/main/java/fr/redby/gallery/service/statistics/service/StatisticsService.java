package fr.redby.gallery.service.statistics.service;

import fr.redby.gallery.service.statistics.beans.DiskUsage;
import fr.redby.gallery.service.statistics.beans.SizePerYear;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class StatisticsService {

    private static final long GB = 1024*1024*1024;

    public DiskUsage getDiskUsage() {
        File root = new File(System.getProperty("GALLERY_PATH"));
        System.out.println("Total space in "+System.getProperty("GALLERY_PATH")+": " + root.getTotalSpace());
        System.out.println("Free space in "+System.getProperty("GALLERY_PATH")+": " + root.getFreeSpace());
        System.out.println("Usable space in "+System.getProperty("GALLERY_PATH")+": " + root.getUsableSpace());

        int used = (int) (root.getTotalSpace()/GB - root.getFreeSpace()/GB);
        int available = (int) (root.getTotalSpace() / GB);

        return new DiskUsage(used, available);
    }

    public SizePerYear getSizePerYear() throws IOException {
        SizePerYear result = new SizePerYear();
        File root = new File(System.getProperty("GALLERY_PATH"));
        for (File folder : root.listFiles(f -> f.isDirectory())) {
            if (!folder.getName().matches("[0-9]+")) continue;
            int folderSize = (int) (Files.walk(folder.toPath())
                    .filter(p -> p.toFile().isFile())
                    .mapToLong(p -> p.toFile().length())
                    .sum() / GB);
            int year = Integer.parseInt(folder.getName());
            System.out.println(year + " => " + folderSize);
            result.getData().add(new SizePerYear.DataType(year, folderSize));
        }

        return result;
    }

}
