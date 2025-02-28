package com.game.backend.services.wiki;

import com.game.backend.models.wiki.WikiArticle;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface WikiArticleService {
    WikiArticle createWikiArticle(WikiArticle article);

    List<WikiArticle> getApprovedArticles();

    List<WikiArticle> getPendingArticles();

    List<WikiArticle> getAllArticles();

    void approveArticle(Long articleId, UserDetails userDetails);

    void rejectArticle(Long articleId, UserDetails userDetails);

    WikiArticle updateWikiArticle(Long articleId, WikiArticle updatedCategory);

    void deleteWikiArticle(Long articleId);
}
