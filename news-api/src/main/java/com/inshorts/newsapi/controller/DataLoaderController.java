package com.inshorts.newsapi.controller;


import com.inshorts.newsapi.service.DataLoaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataLoaderController {

    private final DataLoaderService service;

    public DataLoaderController(DataLoaderService service) {
        this.service = service;
    }

    @PostMapping("/load_json")
    public ResponseEntity<String> loadFromJson(@RequestParam("filePath") String filePath) {
        try {
            int count = service.loadFromJson(filePath);
            return ResponseEntity.ok("Loaded " + count + " articles from JSON.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
