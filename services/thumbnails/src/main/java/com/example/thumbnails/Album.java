package com.example.thumbnails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Album {
    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate date;
    
    @JsonProperty("name")
    public String name;
    
    @JsonProperty("id")
    public String id;

    public Album() {}

    public Album(LocalDate date, String name, String id) {
        this.date = date;
        this.name = name;
        this.id = id;
    }
}
