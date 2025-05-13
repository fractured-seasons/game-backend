package com.game.backend.services.forums.impl;

import com.game.backend.models.User;
import com.game.backend.models.forums.ForumSection;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.forums.ForumSectionRepository;
import com.game.backend.services.forums.ForumSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumSectionServiceImpl implements ForumSectionService {
    @Autowired
    ForumSectionRepository forumSectionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ForumSection createForumSection(ForumSection forumSection, String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        forumSection.setCreatedBy(user);
        return forumSectionRepository.save(forumSection);
    }

    @Override
    public ForumSection getForumSectionById(Long id) {
        return forumSectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum section not found"));
    }

    @Override
    public List<ForumSection> getAllForumSections() {
        return forumSectionRepository.findAll();
    }

    @Override
    public ForumSection updateForumSection(Long id, String updatedName) {
        ForumSection section = forumSectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum section not found"));

        section.setName(updatedName.toUpperCase());
        return forumSectionRepository.save(section);
    }

    @Override
    public void deleteSection(Long id) {
        forumSectionRepository.deleteById(id);
    }
}
