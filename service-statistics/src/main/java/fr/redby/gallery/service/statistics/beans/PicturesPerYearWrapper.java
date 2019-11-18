package fr.redby.gallery.service.statistics.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PicturesPerYearWrapper implements Serializable {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private List<PicturesPerYear> data;
    private String name = "Number of pictures per year";

    public PicturesPerYearWrapper(final List<PicturesPerYear> data) {
        this.date = new Date();
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public List<PicturesPerYear> getData() {
        return data;
    }

    public String getName() {
        return name;
    }
}
