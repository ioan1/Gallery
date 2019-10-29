package fr.redby.gallery.service.statistics.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.*;

public class SizePerYear {

    public static class DataType implements Comparable<DataType> {
        public long x;
        public int y;

        public DataType(int year, int folderSize) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.DAY_OF_YEAR, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            this.x = cal.getTime().getTime();
            this.y = folderSize;
        }

        @Override public int compareTo(DataType o) {
            return new Long(x).compareTo(new Long(o.x));
        }
    }

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private List<DataType> data;
    private String unit = "GB";
    private String name = "Disk usage (GB) per year";

    public SizePerYear() {
        this.date = new Date();
        this.data = new ArrayList<>();
    }

    public Date getDate() {
        return date;
    }

    public List<DataType> getData() {
        return this.data;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

}
