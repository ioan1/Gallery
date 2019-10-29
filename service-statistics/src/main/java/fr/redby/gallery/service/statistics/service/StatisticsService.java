package fr.redby.gallery.service.statistics.service;

import fr.redby.gallery.service.statistics.beans.DiskUsage;
import org.springframework.stereotype.Service;

import java.io.File;

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

}
