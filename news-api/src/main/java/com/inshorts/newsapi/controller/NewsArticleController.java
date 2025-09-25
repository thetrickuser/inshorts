package com.inshorts.newsapi.controller;

import com.inshorts.newsapi.model.NewsArticleDTO;
import com.inshorts.newsapi.service.NewsArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsArticleController {

    private final NewsArticleService service;

    public NewsArticleController(NewsArticleService service) {
        this.service = service;
    }

    @GetMapping("/category")
    public List<NewsArticleDTO> getByCategory(@RequestParam String category) {
        return service.getByCategory(category);
    }

    @GetMapping("/score")
    public List<NewsArticleDTO> getByMinScore(@RequestParam double min_score) {
        return service.getByMinScore(min_score);
    }

    @GetMapping("/search")
    public List<NewsArticleDTO> searchByQuery(@RequestParam String query) {
        return service.searchByQuery(query);
    }

    @GetMapping("/source")
    public List<NewsArticleDTO> getBySource(@RequestParam String source) {
        return service.getBySource(source);
    }

    @GetMapping("/nearby")
    public List<NewsArticleDTO> getNearby(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        return service.getNearby(lat, lon, radius);
    }
}

