package com.inshorts.newsapi.service;

import com.inshorts.newsapi.entity.NewsArticle;
import com.inshorts.newsapi.mapper.NewsArticleMapper;
import com.inshorts.newsapi.model.NewsArticleDTO;
import com.inshorts.newsapi.repository.NewsArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsArticleService {

    private static final double TITLE_WEIGHT = 2.0;
    private static final Set<String> STOPWORDS = Set.of(
            "the", "and", "a", "an", "of", "in", "on", "about", "for", "to", "with", "at", "by", "from"
    );

    private final NewsArticleRepository repository;

    public NewsArticleService(NewsArticleRepository repository) {
        this.repository = repository;
    }

    public List<NewsArticleDTO> getByCategory(String category) {
        return repository.findByCategoryIgnoreCaseOrderByPublicationDateDesc(category)
                .stream().map(NewsArticleMapper::toDTO).limit(5).toList();
    }

    public List<NewsArticleDTO> getByMinScore(double minScore) {
        return repository.findByRelevanceScoreGreaterThanEqualOrderByRelevanceScoreDesc(minScore)
                .stream().map(NewsArticleMapper::toDTO).toList();
    }

    public List<NewsArticleDTO> searchByQuery(String query) {
        // 1. Tokenize and clean query
        List<String> keywords = Arrays.stream(query.toLowerCase().split("\\W+"))
                .filter(k -> k.length() > 2 && !STOPWORDS.contains(k))
                .toList();

        if (keywords.isEmpty()) return Collections.emptyList();

        // 2. Map to store article scores
        Map<NewsArticle, Double> articleScores = new HashMap<>();

        // 3. Iterate keywords
        for (String keyword : keywords) {
            List<NewsArticle> matchedArticles = repository.findByKeyword(keyword);
            for (NewsArticle article : matchedArticles) {
                double score = articleScores.getOrDefault(article, 0.0);

                // Count occurrences in title
                int titleMatches = countOccurrences(article.getTitle(), keyword);
                // Count occurrences in description
                int descMatches = countOccurrences(article.getDescription(), keyword);

                // Calculate weighted score
                score += TITLE_WEIGHT * titleMatches + descMatches;

                // Multiply by relevanceScore from DB
                score *= article.getRelevanceScore();

                articleScores.put(article, score);
            }
        }

        // 4. Sort articles by final score descending
        List<NewsArticle> sortedArticles = articleScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        // 5. Convert to DTOs
        return sortedArticles.stream()
                .map(NewsArticleMapper::toDTO)
                .limit(5)
                .collect(Collectors.toList());
    }

    // Utility method to count keyword occurrences in text
    private int countOccurrences(String text, String keyword) {
        if (text == null) return 0;
        text = text.toLowerCase();
        int count = 0, idx = 0;
        while ((idx = text.indexOf(keyword, idx)) != -1) {
            count++;
            idx += keyword.length();
        }
        return count;
    }

    public List<NewsArticleDTO> getBySource(String source) {
        return repository.findBySourceNameIgnoreCaseOrderByPublicationDateDesc(source)
                .stream().map(NewsArticleMapper::toDTO).toList();
    }

    public List<NewsArticleDTO> getNearby(double lat, double lon, double radiusKm) {
        List<NewsArticle> all = repository.findAll();
        return all.stream().filter(a -> haversine(lat, lon, a.getLatitude(), a.getLongitude()) <= radiusKm)
                .sorted(Comparator.comparingDouble(a -> haversine(lat, lon, a.getLatitude(), a.getLongitude())))
                .map(NewsArticleMapper::toDTO)
                .collect(Collectors.toList());
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}

