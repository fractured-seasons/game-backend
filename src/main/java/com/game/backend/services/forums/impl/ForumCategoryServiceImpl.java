package com.game.backend.services.forums.impl;

import com.game.backend.dtos.forum.CategoryDTO;
import com.game.backend.models.User;
import com.game.backend.models.forums.ForumCategory;
import com.game.backend.models.forums.ForumSection;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.forums.ForumCategoryRepository;
import com.game.backend.repositories.forums.ForumSectionRepository;
import com.game.backend.services.forums.ForumCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumCategoryServiceImpl implements ForumCategoryService {
    @Autowired
    ForumCategoryRepository forumCategoryRepository;

    @Autowired
    ForumSectionRepository forumSectionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ForumCategory createForumCategory(CategoryDTO forumCategory, String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        ForumSection section = forumSectionRepository.findById(forumCategory.getSectionId())
                .orElseThrow(() -> new RuntimeException("Forum section not found"));
        
        ForumCategory newCategory = new ForumCategory();
        newCategory.setName(forumCategory.getName());
        newCategory.setDescription(forumCategory.getDescription());
        newCategory.setCreatedBy(user);
        newCategory.setSection(section);
        return forumCategoryRepository.save(newCategory);
    }

    @Override
    public ForumCategory getForumCategoryById(Long id) {
        return forumCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum category not found"));
    }

    @Override
    public List<ForumCategory> getAllForumCategories() {
        return forumCategoryRepository.findAll();
    }

    @Override
    public ForumCategory updateForumCategory(Long id, CategoryDTO updatedCategory) {
        ForumCategory category = forumCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum category not found"));

        ForumSection section = forumSectionRepository.findById(updatedCategory.getSectionId())
                .orElseThrow(() -> new RuntimeException("Forum section not found"));

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        category.setSection(section);
        return forumCategoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        forumCategoryRepository.deleteById(id);
    }
}
