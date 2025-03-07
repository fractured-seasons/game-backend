    package com.game.backend.controllers.wiki;

    import com.game.backend.dtos.wiki.ArticleDTO;
    import com.game.backend.dtos.wiki.ArticleResponseDTO;
    import com.game.backend.models.wiki.WikiArticle;
    import com.game.backend.security.response.ApiResponse;
    import com.game.backend.services.wiki.WikiArticleService;
    import jakarta.validation.Valid;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
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
        public ResponseEntity<?> createWikiArticle(@RequestBody @Valid ArticleDTO article, @AuthenticationPrincipal UserDetails userDetails) {
            WikiArticle createdWikiArticle = wikiArticleService.createWikiArticle(article, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdWikiArticle);
        }

        @GetMapping("/{articleSlug}")
        public ResponseEntity<WikiArticle> getArticleBySlug(@PathVariable String articleSlug) {
            return ResponseEntity.ok(wikiArticleService.getWikiArticleBySlug(articleSlug));
        }

        @GetMapping()
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
        public ResponseEntity<Page<ArticleResponseDTO>> getAllArticles(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ArticleResponseDTO> wikiArticles = wikiArticleService.getAllArticles(pageable);

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

        @PutMapping("/{articleId}/toggle-visibility")
        @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
        public ResponseEntity<?> toggleArticleVisibility(@PathVariable Long articleId, @AuthenticationPrincipal UserDetails userDetails) {
            wikiArticleService.toggleArticleVisibility(articleId, userDetails);
            return ResponseEntity.ok(new ApiResponse(true, "Article visibility toggled successfully"));
        }

        @PutMapping("/{articleId}")
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR', 'WIKI_CONTRIBUTOR')")
        public ResponseEntity<?> updateWikiArticle(@PathVariable Long articleId, @RequestBody @Valid ArticleDTO articleDTO, @AuthenticationPrincipal UserDetails userDetails) {
            WikiArticle updatedWikiCategory = wikiArticleService.updateWikiArticle(articleId, articleDTO, userDetails);
            return ResponseEntity.ok(updatedWikiCategory);
        }

        @DeleteMapping("/{articleId}")
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
        public ResponseEntity<ApiResponse> deleteWikiCategoryById(@PathVariable Long articleId) {
            wikiArticleService.deleteWikiArticleById(articleId);
            return ResponseEntity.ok(new ApiResponse(true, "Wiki article deleted successfully"));
        }
    }

