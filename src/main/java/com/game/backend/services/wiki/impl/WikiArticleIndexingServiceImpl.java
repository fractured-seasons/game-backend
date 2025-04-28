package com.game.backend.services.wiki.impl;

import com.game.backend.dtos.wiki.ArticleIndexDTO;
import com.game.backend.mappers.WikiArticleIndexMapper;
import com.game.backend.models.wiki.WikiArticle;
import com.game.backend.search.WikiArticleSearchRepository;
import com.game.backend.services.wiki.WikiArticleIndexingService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class WikiArticleIndexingServiceImpl implements WikiArticleIndexingService {
    private final WikiArticleSearchRepository wikiArticleSearchRepository;
    private final WikiArticleIndexMapper wikiArticleIndexMapper;

    public WikiArticleIndexingServiceImpl(final WikiArticleSearchRepository wikiArticleSearchRepository,
                                          final WikiArticleIndexMapper wikiArticleIndexMapper) {
        this.wikiArticleSearchRepository = wikiArticleSearchRepository;
        this.wikiArticleIndexMapper = wikiArticleIndexMapper;
    }

    @Async("indexExecutor")
    @Override
    public CompletableFuture<Void> indexWikiArticle(WikiArticle article) {
        System.out.println("[START] " + Thread.currentThread().getName() + " – articol " + article.getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        wikiArticleSearchRepository.save(wikiArticleIndexMapper.toIndexDTO(article));
        System.out.println("[END] " + Thread.currentThread().getName() + " – articol " + article.getId());
        return CompletableFuture.completedFuture(null);
    }

//    @Override
//    public void indexWikiArticle(WikiArticle article) {
//        ArticleIndexDTO articleIndexDTO = wikiArticleIndexMapper.toIndexDTO(article);
//        wikiArticleSearchRepository.save(articleIndexDTO);
//    }

    @Override
    public void removeIndexedArticle(Long articleId) {
        wikiArticleSearchRepository.deleteById(articleId);
    }
}
