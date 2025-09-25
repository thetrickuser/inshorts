package com.inshorts.newsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticleDTO {
    private UUID id;
    private String title;
    private String description;
    private String url;

    @JsonProperty("publication_date")
    private String publicationDate;

    @JsonProperty("source_name")
    private String sourceName;
    private List<String> category;

    @JsonProperty("relevance_score")
    private double relevanceScore;
    private double latitude;
    private double longitude;
}
