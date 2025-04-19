package com.game.backend.services.wiki;

import com.game.backend.dtos.wiki.ArticleDTO;
import com.game.backend.dtos.wiki.ArticleIndexDTO;
import com.game.backend.dtos.wiki.ArticleResponseDTO;
import com.game.backend.models.wiki.WikiArticle;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface WikiArticleService {
    WikiArticle createWikiArticle(ArticleDTO article, UserDetails userDetails);

    List<WikiArticle> getApprovedArticles();

    List<WikiArticle> getPendingArticles();

    Page<ArticleResponseDTO> getAllArticles(Pageable pageable);

    void approveArticle(Long articleId, UserDetails userDetails);

    void rejectArticle(Long articleId, UserDetails userDetails);

    WikiArticle updateWikiArticle(Long articleId, @Valid ArticleDTO updatedArticle, UserDetails userDetails);

    void deleteWikiArticleById(Long articleId);

    WikiArticle getWikiArticleBySlug(String articleSlug);

    void toggleArticleVisibility(Long articleId, UserDetails userDetails);

    List<ArticleIndexDTO> searchTopics(String query);
}
