package com.game.backend.services.wiki;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public class SearchEndpointPerformanceTest {

    @LocalServerPort
    int port;

    @MockBean
    private WikiArticleService wikiArticleService;

    private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port + "/api/wiki/article/search?query=test";

        given(wikiArticleService.searchTopics(anyString()))
            .willReturn(Collections.emptyList());
    }

    @Test
    void sequentialSearch() {
        long start = System.currentTimeMillis();

        IntStream.range(0, 10).forEach(i -> {
            ResponseEntity<List> resp = restTemplate.getForEntity(baseUrl, List.class);
            System.out.println("Seq req " + i +
                    " → status: " + resp.getStatusCodeValue() +
                    ", body: "   + resp.getBody());
        });
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Search secvenţial: " + elapsed + " ms");
    }

    @Test
    void parallelSearch() {
        long start = System.currentTimeMillis();
        CompletableFuture<?>[] futures = IntStream.range(0, 10)
            .mapToObj(i -> CompletableFuture.runAsync(() -> {
                ResponseEntity<List> resp = restTemplate.getForEntity(baseUrl, List.class);
                System.out.println("Par req [" + Thread.currentThread().getName() + "]" +
                        " → status: " + resp.getStatusCodeValue() +
                        ", body: "   + resp.getBody());
            }))
            .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Search paralel:   " + elapsed + " ms");
    }
}
