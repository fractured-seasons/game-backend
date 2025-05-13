package com.game.backend.controllers.wiki;

import com.game.backend.security.response.UserDetailsResponse;
import com.game.backend.services.wiki.WikiContributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/wiki")
public class WikiContributionController {
    private final WikiContributionService wikiContributionService;

    public WikiContributionController(final WikiContributionService wikiContributionService) {
        this.wikiContributionService = wikiContributionService;
    }

    @GetMapping("/contributors")
    public ResponseEntity<List<UserDetailsResponse>> getWikiContributors() {
        List<UserDetailsResponse> contributors = wikiContributionService.getWikiContributors();
        return ResponseEntity.ok(contributors);
    }

    @GetMapping("/{slug}/contributors")
    public ResponseEntity<List<UserDetailsResponse>> getArticleContributors(@PathVariable String slug) {
        List<UserDetailsResponse> contributors = wikiContributionService.getArticleContributors(slug);
        return ResponseEntity.ok(contributors);
    }
}
