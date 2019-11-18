package fr.redby.gallery.service.statistics.beans;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Calendar;

public class PicturesPerYear implements Serializable, Comparable<PicturesPerYear> {

    @Id
    public long x;
    public int y;

    public long getX() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, (int) this.x);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime().getTime();
    }

    public int getY() {
        return y;
    }

    @Override public int compareTo(PicturesPerYear o) {
        return new Long(x).compareTo(new Long(o.x));
    }
}
