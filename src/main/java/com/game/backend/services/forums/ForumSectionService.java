package com.game.backend.services.forums;

import com.game.backend.models.forums.ForumSection;

import java.util.List;

public interface ForumSectionService {
    ForumSection createForumSection(ForumSection forumSection, String username);

    ForumSection getForumSectionById(Long id);

    List<ForumSection> getAllForumSections();

    ForumSection updateForumSection(Long id, String updatedName);

    void deleteSection(Long id);
}
