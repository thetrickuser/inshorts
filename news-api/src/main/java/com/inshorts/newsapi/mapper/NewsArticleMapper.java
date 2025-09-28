package com.inshorts.newsapi.mapper;

import com.inshorts.newsapi.entity.NewsArticle;
import com.inshorts.newsapi.model.NewsArticleDTO;

import java.time.LocalDateTime;

public class NewsArticleMapper {

    public static NewsArticle toEntity(NewsArticleDTO dto) {
        return new NewsArticle(
            dto.getId(),
            dto.getTitle(),
            dto.getDescription(),
            dto.getUrl(),
            LocalDateTime.parse(dto.getPublicationDate()),
            dto.getSourceName(),
            dto.getCategory(),
            dto.getRelevanceScore(),
            dto.getLatitude(),
            dto.getLongitude()
        );
    }

    public static NewsArticleDTO toDTO(NewsArticle entity) {
        return new NewsArticleDTO(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getUrl(),
            entity.getPublicationDate().toString(),
            entity.getSourceName(),
            entity.getCategory(),
            entity.getRelevanceScore(),
            entity.getLatitude(),
            entity.getLongitude(),
                null
        );
    }
}
