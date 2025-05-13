package com.game.backend.services.wiki;

import com.game.backend.models.wiki.WikiArticle;

import java.util.concurrent.CompletableFuture;

public interface WikiArticleIndexingService {
//    void indexWikiArticle(WikiArticle article);
    CompletableFuture<Void> indexWikiArticle(WikiArticle article);
    void removeIndexedArticle(Long articleId);
}
