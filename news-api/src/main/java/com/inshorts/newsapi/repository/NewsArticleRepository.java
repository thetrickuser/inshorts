package com.inshorts.newsapi.repository;

import com.inshorts.newsapi.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, UUID> {
    @Query("SELECT n FROM NewsArticle n JOIN n.category c WHERE LOWER(c) = LOWER(:category) ORDER BY n.publicationDate DESC")
    List<NewsArticle> findByCategoryIgnoreCaseOrderByPublicationDateDesc(@Param("category") String category);
    List<NewsArticle> findByRelevanceScoreGreaterThanEqualOrderByRelevanceScoreDesc(double minScore);
    List<NewsArticle> findBySourceNameIgnoreCaseOrderByPublicationDateDesc(String sourceName);
    @Query("SELECT n FROM NewsArticle n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(n.description) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY n.relevanceScore DESC")
    List<NewsArticle> searchByQuery(@Param("query") String query);

    @Query("SELECT n FROM NewsArticle n " +
            "WHERE LOWER(n.title) LIKE %:keyword% " +
            "OR LOWER(n.description) LIKE %:keyword%")
    List<NewsArticle> findByKeyword(@Param("keyword") String keyword);
}
