package com.game.backend.services.forums.impl;

import com.game.backend.models.User;
import com.game.backend.models.forums.ForumCategory;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.forums.ForumCategoryRepository;
import com.game.backend.services.forums.ForumCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumCategoryServiceImpl implements ForumCategoryService {
    @Autowired
    ForumCategoryRepository forumCategoryRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ForumCategory createForumCategory(ForumCategory forumCategory, String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        forumCategory.setCreatedBy(user);
        return forumCategoryRepository.save(forumCategory);
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
    public ForumCategory updateForumCategory(Long id, ForumCategory updatedCategory) {
        ForumCategory category = forumCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum category not found"));

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        category.setSection(updatedCategory.getSection());
        return forumCategoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        forumCategoryRepository.deleteById(id);
    }
}
