package com.inshorts.newsapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "news_articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticle {
    @Id
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String url;
    private LocalDateTime publicationDate;
    private String sourceName;

    @ElementCollection
    @CollectionTable(name = "article_categories", joinColumns = @JoinColumn(name = "article_id"))
    private List<String> category;
    private double relevanceScore;
    private double latitude;
    private double longitude;
}
