package com.game.backend.services.wiki;

import com.game.backend.models.wiki.WikiArticle;

public interface WikiArticleIndexingService {
    void indexWikiArticle(WikiArticle article);
    void removeIndexedArticle(Long articleId);
}
