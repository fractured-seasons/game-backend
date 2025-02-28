package com.game.backend.controllers.wiki;

import com.game.backend.models.wiki.WikiCategory;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.wiki.WikiCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wiki/category")
public class WikiCategoryController {
    private final WikiCategoryService wikiCategoryService;

    public WikiCategoryController(final WikiCategoryService wikiCategoryService) {
        this.wikiCategoryService = wikiCategoryService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> createWikiCategory(@RequestBody @Valid WikiCategory wikiCategory, @AuthenticationPrincipal UserDetails userDetails) {
        WikiCategory createdWikiCategory = wikiCategoryService.createWikiCategory(wikiCategory, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWikiCategory);
    }

    public
    @GetMapping("/{categoryId}") ResponseEntity<WikiCategory> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(wikiCategoryService.getWikiCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<WikiCategory>> getAllCategories() {
        List<WikiCategory> wikiCategories = wikiCategoryService.getAllForumCategories();
        return new ResponseEntity<>(wikiCategories, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> updateWikiCategory(@PathVariable Long categoryId, @RequestBody @Valid WikiCategory updatedCategory) {
        WikiCategory updatedWikiCategory = wikiCategoryService.updateWikiCategory(categoryId, updatedCategory);
        return ResponseEntity.ok(updatedWikiCategory);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<ApiResponse> deleteWikiCategory(@PathVariable Long categoryId) {
        wikiCategoryService.deleteWikiCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse(true, "Wiki category deleted successfully"));
    }
}
