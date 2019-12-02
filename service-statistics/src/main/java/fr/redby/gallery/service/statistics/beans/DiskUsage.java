package fr.redby.gallery.service.statistics.beans;

/**
 * Entity holding the disk usage statistics (used vs available).
 * @author FITSDEV3
 * @version $Revision$
 */
@SuppressWarnings("squid:S1068")
public class DiskUsage {

    private int used, available;
    private String unit = "GB";

    public DiskUsage(int used, int available) {
        this.used = used;
        this.available = available;
    }

    public int getUsed() {
        return used;
    }

    public int getAvailable() {
        return available;
    }

    public String getUnit() {
        return unit;
    }

}
