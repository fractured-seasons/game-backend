package com.game.backend.services.wiki.impl;

import com.game.backend.models.wiki.WikiCategory;
import com.game.backend.services.wiki.WikiCategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WikiCategoryServiceImpl implements WikiCategoryService {
    @Override
    public WikiCategory createWikiCategory(@Valid WikiCategory forumCategory, String username) {
        return null;
    }

    @Override
    public WikiCategory getWikiCategoryById(Long categoryId) {
        return null;
    }

    @Override
    public List<WikiCategory> getAllForumCategories() {
        return null;
    }

    @Override
    public WikiCategory updateWikiCategory(Long categoryId, @Valid WikiCategory updatedCategory) {
        return null;
    }

    @Override
    public void deleteWikiCategory(Long categoryId) {

    }
}
