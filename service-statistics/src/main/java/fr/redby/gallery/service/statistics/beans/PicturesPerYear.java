package fr.redby.gallery.service.statistics.beans;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Calendar;

public class PicturesPerYear implements Serializable, Comparable<PicturesPerYear> {

    @Id
    public long x;
    public int y;

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

    @Override public int compareTo(PicturesPerYear o) {
        return new Long(x).compareTo(new Long(o.x));
    }
}