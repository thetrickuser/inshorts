package com.inshorts.newsapi.controller;

import com.inshorts.newsapi.model.LlmIntentResponse;
import com.inshorts.newsapi.model.NewsArticleDTO;
import com.inshorts.newsapi.service.LLMService;
import com.inshorts.newsapi.service.NewsArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsArticleController {

    private final NewsArticleService service;
    private final LLMService llmService;

    public NewsArticleController(NewsArticleService service, LLMService llmService) {
        this.service = service;
        this.llmService = llmService;
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

    @GetMapping("/query")
    public ResponseEntity<?> queryNews(
            @RequestParam String text,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon) {

        LlmIntentResponse parsed = llmService.processQuery(text);
        List<NewsArticleDTO> articles;

        switch (parsed.getIntent()) {
            case "category":
                articles = service.getByCategory(String.valueOf(parsed.getEntities()));
                break;
            case "source":
                articles = service.getBySource(String.valueOf(parsed.getEntities()));
                break;
            case "nearby":
                if (lat == null || lon == null) {
                    return ResponseEntity.badRequest().body("Latitude and longitude required for 'nearby' intent.");
                }
                articles = service.getNearby(lat, lon, 10);
                break;
            case "score":
                articles = service.getByMinScore(0.7);
                break;
            case "search":
            default:
                articles = service.searchByQuery(String.valueOf(parsed.getEntities()));
        }

        // Add LLM summaries
        articles.forEach(a -> a.setLlmSummary(llmService.summarizeArticle(a.getDescription())));

        return ResponseEntity.ok(Map.of("articles", articles));
    }
}

