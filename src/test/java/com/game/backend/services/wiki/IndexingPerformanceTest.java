package com.game.backend.services.wiki;

import com.game.backend.models.wiki.WikiArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class IndexingPerformanceTest {

    @Autowired
    private WikiArticleIndexingService indexingService;

    private List<WikiArticle> articles;

    @BeforeEach
    void setup() {
        articles = IntStream.range(0, 10)
            .mapToObj(i -> {
                WikiArticle a = new WikiArticle();
                a.setId((long) i);
                a.setTitle("Test " + i);
                a.setContent("Conținut test " + i);
                return a;
            })
            .collect(Collectors.toList());
    }

    @Test
    void testSequentialIndexing() {
        long start = System.currentTimeMillis();

        for (WikiArticle art : articles) {
            indexingService.indexWikiArticle(art).join();
        }
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Indexare secvențială: " + elapsed + " ms");
    }

    @Test
    void testParallelIndexing() {
        long start = System.currentTimeMillis();

        List<CompletableFuture<Void>> futures = articles.stream()
            .map(indexingService::indexWikiArticle)
            .toList();

        CompletableFuture
            .allOf(futures.toArray(new CompletableFuture[0]))
            .join();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Indexare paralelă: " + elapsed + " ms");
    }
}
