//package com.inshorts.newsapi.controller;
//
//import com.inshorts.newsapi.model.LlmIntentResponse;
//import com.inshorts.newsapi.model.NewsArticleDTO;
//import com.inshorts.newsapi.service.LLMService;
//import com.inshorts.newsapi.service.NewsArticleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/news")
//@RequiredArgsConstructor
//public class NewsQueryController {
//
//    private final LLMService llmService;
//    private final NewsArticleService newsService; // your existing service for DB queries
//
//    @GetMapping("/query")
//    public ResponseEntity<?> queryNews(
//            @RequestParam String text,
//            @RequestParam(required = false) Double lat,
//            @RequestParam(required = false) Double lon) {
//
//        LlmIntentResponse parsed = llmService.processQuery(text);
//        List<NewsArticleDTO> articles;
//
//        switch (parsed.getIntent()) {
//            case "category":
//                articles = newsService.getByCategory(String.valueOf(parsed.getEntities()));
//                break;
//            case "source":
//                articles = newsService.getBySource(String.valueOf(parsed.getEntities()));
//                break;
//            case "nearby":
//                if (lat == null || lon == null) {
//                    return ResponseEntity.badRequest().body("Latitude and longitude required for 'nearby' intent.");
//                }
//                articles = newsService.getNearby(lat, lon, 10);
//                break;
//            case "score":
//                articles = newsService.getByMinScore(0.7);
//                break;
//            case "search":
//            default:
//                articles = newsService.searchByQuery(String.valueOf(parsed.getEntities()));
//        }
//
//        // Add LLM summaries
//        articles.forEach(a -> a.setLlmSummary(llmService.summarizeArticle(a.getDescription())));
//
//        return ResponseEntity.ok(Map.of("articles", articles));
//    }
//}
