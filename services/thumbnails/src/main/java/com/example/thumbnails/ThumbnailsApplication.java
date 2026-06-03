package com.example.thumbnails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ThumbnailsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThumbnailsApplication.class, args);
    }

    @GetMapping("/health")
    public String health() {
        return "{\"status\":\"UP\"}";
    }

    @GetMapping("/")
    public String index() {
        return "{\"service\":\"Thumbnails\",\"version\":\"1.0.0\"}";
    }
}
