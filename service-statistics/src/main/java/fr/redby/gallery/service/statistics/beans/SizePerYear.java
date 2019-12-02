package fr.redby.gallery.service.statistics.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.redby.gallery.service.statistics.repositories.SizePerYearRepository;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.*;

/**
 * Wrapper class for the size per year graph.
 */
@SuppressWarnings("squid:S1068")
public class SizePerYear implements Serializable {

    @Id
    private Integer id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private List<DataType> data;
    private String unit = "GB";
    private String name = "Disk usage (GB) per year";

    /**
     * Default constructor initializing all the fields.
     */
    public SizePerYear() {
        this.date = new Date();
        this.data = new ArrayList<>();
        this.id = SizePerYearRepository.STAT_IDENTIFIER;
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

}
