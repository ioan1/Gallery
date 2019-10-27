/**
 * Copyright European Commission's
 * Taxation and Customs Union Directorate-General (DG TAXUD).
 */
package fr.redby.gallery.service.statistics.beans;

/**
 * TODO.
 * @author FITSDEV3
 * @version $Revision$
 */
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
