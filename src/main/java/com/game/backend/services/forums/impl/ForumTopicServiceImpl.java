package com.game.backend.services.forums.impl;

import com.game.backend.models.forums.ForumTopic;
import com.game.backend.repositories.forums.ForumTopicRepository;
import com.game.backend.services.forums.ForumTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ForumTopicServiceImpl implements ForumTopicService {
    @Autowired
    ForumTopicRepository forumTopicRepository;

    @Override
    public Page<ForumTopic> getAllTopics(Long categoryId, Pageable pageable, UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR") || auth.getAuthority().equals("ROLE_SUPPORT"));

        return isAdmin ? forumTopicRepository.findByCategoryIdOrderByPinnedDesc(categoryId, pageable) : forumTopicRepository.findByCategoryIdAndHiddenFalseOrderByPinnedDesc(categoryId, pageable);
    }

    @Override
    public ForumTopic getForumTopicById(Long id) {
        return forumTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum topic not found"));
    }
}
