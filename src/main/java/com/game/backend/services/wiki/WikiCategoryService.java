package com.game.backend.services.wiki;

import com.game.backend.models.wiki.WikiCategory;
import jakarta.validation.Valid;

import java.util.List;

public interface WikiCategoryService {
    WikiCategory createWikiCategory(@Valid WikiCategory wikiCategory, String username);

    WikiCategory getWikiCategoryById(Long categoryId);

    List<WikiCategory> getAllWikiCategoriesWithApprovedArticles();

    WikiCategory updateWikiCategory(Long categoryId, @Valid WikiCategory updatedCategory);

    void deleteWikiCategory(Long categoryId);

    List<WikiCategory> getAllWikiCategories();
}
