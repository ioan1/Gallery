package fr.redby.gallery.service.statistics.beans;

import java.io.Serializable;
import java.util.Calendar;

public class DataType implements Serializable, Comparable<DataType> {
    public long x;
    public int y;

    public DataType() {
        super();
    }

    public DataType(int year, int folderSize) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        this.x = cal.getTime().getTime();
        this.y = folderSize;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override public int compareTo(DataType o) {
        return new Long(x).compareTo(new Long(o.x));
    }
}