package fr.redby.gallery.service.statistics.beans;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Wrapper class for the pictures per year statistics graph.
 */
public class PicturesPerYear implements Serializable, Comparable<PicturesPerYear> {

    @Id
    private long x;
    private int y;

    /**
     * Gets the X axis value.
     * @return the X axis value formatted as a timestamp (1st of january of the given year).
     */
    public long getX() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, (int) this.x);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime().getTime();
    }

    @Override
    public int compareTo(PicturesPerYear o) {
        return new Long(x).compareTo(new Long(o.x));
    }
}
