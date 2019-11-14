package fr.redby.gallery.service.statistics.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.redby.gallery.service.statistics.repositories.SizePerYearRepository;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.*;

public class SizePerYear implements Serializable {

    @Id
    private Integer id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private List<DataType> data;
    private String unit = "GB";
    private String name = "Disk usage (GB) per year";

    public SizePerYear() {
        this.date = new Date();
        this.data = new ArrayList<>();
        this.id = SizePerYearRepository.STAT_IDENTIFIER;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setData(List<DataType> data) {
        this.data = data;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
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
