package com.inshorts.newsapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inshorts.newsapi.entity.NewsArticle;
import com.inshorts.newsapi.mapper.NewsArticleMapper;
import com.inshorts.newsapi.model.NewsArticleDTO;
import com.inshorts.newsapi.repository.NewsArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
public class DataLoaderService {

    private final NewsArticleRepository repository;

    public DataLoaderService(NewsArticleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public int loadFromJson(String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<NewsArticleDTO> articles = mapper.readValue(new File(filePath), new TypeReference<List<NewsArticleDTO>>() {});

        List<NewsArticle> newsArticles = articles.stream().map(NewsArticleMapper::toEntity).toList();
        repository.saveAll(newsArticles);
        return articles.size();
    }
}
