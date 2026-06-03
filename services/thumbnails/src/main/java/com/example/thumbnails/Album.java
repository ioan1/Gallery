package com.example.thumbnails;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Album {
    @JsonProperty("date")
    public String date;
    
    @JsonProperty("name")
    public String name;
    
    @JsonProperty("id")
    public String id;

    public Album() {}

    public Album(String date, String name, String id) {
        this.date = date;
        this.name = name;
        this.id = id;
    }
}
