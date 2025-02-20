package com.game.backend.services.forums;

import com.game.backend.dtos.CategoryDTO;
import com.game.backend.models.forums.ForumCategory;

import java.util.List;

public interface ForumCategoryService {
    ForumCategory createForumCategory(CategoryDTO forumCategory, String username);

    ForumCategory getForumCategoryById(Long id);

    List<ForumCategory> getAllForumCategories();

    ForumCategory updateForumCategory(Long id, CategoryDTO updatedCategory);

    void deleteCategory(Long id);
}
