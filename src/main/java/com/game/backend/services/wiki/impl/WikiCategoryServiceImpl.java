package com.game.backend.services.wiki.impl;

import com.game.backend.models.User;
import com.game.backend.models.wiki.WikiCategory;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.wiki.WikiCategoryRepository;
import com.game.backend.services.wiki.WikiCategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WikiCategoryServiceImpl implements WikiCategoryService {

    private final WikiCategoryRepository wikiCategoryRepository;
    private final UserRepository userRepository;

    public WikiCategoryServiceImpl(final WikiCategoryRepository wikiCategoryRepository, final UserRepository userRepository) {
        this.wikiCategoryRepository = wikiCategoryRepository;
        this.userRepository = userRepository;
    }


    @Override
    public WikiCategory createWikiCategory(@Valid WikiCategory wikiCategory, String username) {
        User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        wikiCategory.setCreatedBy(currentUser);

        return wikiCategoryRepository.save(wikiCategory);
    }

    @Override
    public WikiCategory getWikiCategoryById(Long categoryId) {
        return wikiCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Wiki category not found"));
    }

    @Override
    public List<WikiCategory> getAllWikiCategoriesWithApprovedArticles() {
        return wikiCategoryRepository.findAllWithApprovedArticles();
    }

    @Override
    public List<WikiCategory> getAllWikiCategories() {
        return wikiCategoryRepository.findAll();
    }

    @Override
    public WikiCategory updateWikiCategory(Long categoryId, @Valid WikiCategory updatedCategory) {
        WikiCategory wikiCategory = wikiCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Wiki category not found"));

        wikiCategory.setTitle(updatedCategory.getTitle());
        wikiCategory.setDescription(updatedCategory.getDescription());

        return wikiCategoryRepository.save(wikiCategory);
    }

    @Override
    public void deleteWikiCategory(Long categoryId) {
        wikiCategoryRepository.deleteById(categoryId);
    }
}
