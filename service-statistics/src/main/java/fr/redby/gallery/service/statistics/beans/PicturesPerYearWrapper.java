package fr.redby.gallery.service.statistics.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("squid:S1068")
public class PicturesPerYearWrapper implements Serializable {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private List<PicturesPerYear> data;
    private String name = "Number of pictures per year";

    public PicturesPerYearWrapper(final List<PicturesPerYear> data) {
        this.date = new Date();
        this.data = data;
    }

    public List<PicturesPerYear> getData() {
        return data;
    }

}
