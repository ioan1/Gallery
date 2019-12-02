package fr.redby.gallery.service.statistics.beans;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Class wrapping a X and Y data for 2d graphics visualization.
 */
@SuppressWarnings("squid:S1068")
public class DataType implements Serializable, Comparable<DataType> {

    private long x;
    private int y;

    /**
     * Default constructor.
     */
    public DataType() {
        super();
    }

    /**
     * Default constructor taking as parameter a year (x axis) and a folder size (Y axis).
     * @param year X axis
     * @param folderSize Y axis
     */
    public DataType(final int year, final int folderSize) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        this.x = cal.getTime().getTime();
        this.y = folderSize;
    }

    @Override
    public int compareTo(DataType o) {
        return new Long(x).compareTo(new Long(o.x));
    }
}
