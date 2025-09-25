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

    private final NewsArticleRepository repository;

    public NewsArticleService(NewsArticleRepository repository) {
        this.repository = repository;
    }

    public List<NewsArticleDTO> getByCategory(String category) {
        return repository.findByCategoryIgnoreCaseOrderByPublicationDateDesc(category)
                .stream().map(NewsArticleMapper::toDTO).toList();
    }

    public List<NewsArticleDTO> getByMinScore(double minScore) {
        return repository.findByRelevanceScoreGreaterThanEqualOrderByRelevanceScoreDesc(minScore)
                .stream().map(NewsArticleMapper::toDTO).toList();
    }

    public List<NewsArticleDTO> searchByQuery(String query) {
        return repository.searchByQuery(query)
                .stream().map(NewsArticleMapper::toDTO).toList();
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

