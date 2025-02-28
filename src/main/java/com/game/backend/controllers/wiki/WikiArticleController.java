package com.game.backend.controllers.wiki;

import com.game.backend.models.wiki.WikiArticle;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.wiki.WikiArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wiki/article")
public class WikiArticleController {
    private final WikiArticleService wikiArticleService;

    public WikiArticleController(final WikiArticleService wikiArticleService) {
        this.wikiArticleService = wikiArticleService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'WIKI_CONTRIBUTOR')")
    public ResponseEntity<?> createWikiArticle(@RequestBody WikiArticle article) {
        WikiArticle createdWikiArticle = wikiArticleService.createWikiArticle(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWikiArticle);
    }

    @GetMapping()
    public ResponseEntity<List<WikiArticle>> getAllArticles() {
        List<WikiArticle> wikiArticles = wikiArticleService.getAllArticles();
        return new ResponseEntity<>(wikiArticles, HttpStatus.OK);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<WikiArticle>> getApprovedArticles() {
        List<WikiArticle> wikiArticles = wikiArticleService.getApprovedArticles();
        return new ResponseEntity<>(wikiArticles, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<WikiArticle>> getPendingArticles() {
        List<WikiArticle> wikiArticles = wikiArticleService.getPendingArticles();
        return new ResponseEntity<>(wikiArticles, HttpStatus.OK);
    }

    @PutMapping("/{articleId}/approve")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> approveArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetails userDetails) {
        wikiArticleService.approveArticle(articleId, userDetails);
        return ResponseEntity.ok(new ApiResponse(true, "Article approved successfully"));
    }

    @PutMapping("/{articleId}/reject")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> rejectArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetails userDetails) {
        wikiArticleService.rejectArticle(articleId, userDetails);
        return ResponseEntity.ok(new ApiResponse(true, "Article rejected successfully"));
    }

    @PutMapping("/{articleId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> updateWikiArticle(@PathVariable Long articleId, @RequestBody @Valid WikiArticle updatedCategory) {
        WikiArticle updatedWikiCategory = wikiArticleService.updateWikiArticle(articleId, updatedCategory);
        return ResponseEntity.ok(updatedWikiCategory);
    }

    @DeleteMapping("/{articleId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<ApiResponse> deleteWikiCategory(@PathVariable Long articleId) {
        wikiArticleService.deleteWikiArticle(articleId);
        return ResponseEntity.ok(new ApiResponse(true, "Wiki article deleted successfully"));
    }
}

