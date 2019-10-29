package fr.redby.gallery.service.statistics.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Map;

public class SizePerYear {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private Map<Integer, Integer> sizePerYear;
    private String unit = "GB";

    public SizePerYear(Map<Integer, Integer> sizePerYear) {
        this.date = new Date();
        this.sizePerYear = sizePerYear;
    }

    public Date getDate() {
        return date;
    }

    public Map<Integer, Integer> getSizePerYear() {
        return sizePerYear;
    }

    public String getUnit() {
        return unit;
    }

}
