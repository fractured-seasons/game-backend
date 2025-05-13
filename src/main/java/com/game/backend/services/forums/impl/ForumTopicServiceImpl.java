package com.game.backend.services.forums.impl;

import com.game.backend.dtos.forum.TopicIndexDTO;
import com.game.backend.dtos.forum.TopicDTO;
import com.game.backend.mappers.ForumTopicIndexMapper;
import com.game.backend.models.User;
import com.game.backend.models.forums.ForumCategory;
import com.game.backend.models.forums.ForumTopic;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.forums.ForumCategoryRepository;
import com.game.backend.repositories.forums.ForumTopicRepository;
import com.game.backend.search.ForumTopicSearchRepository;
import com.game.backend.services.forums.ForumTopicIndexingService;
import com.game.backend.services.forums.ForumTopicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ForumTopicServiceImpl implements ForumTopicService {
    private final ForumTopicRepository forumTopicRepository;

    private final UserRepository userRepository;

    private final ForumCategoryRepository forumCategoryRepository;
    private final ForumTopicIndexingService forumTopicIndexingService;
    private final ForumTopicSearchRepository forumTopicSearchRepository;
    private final ForumTopicIndexMapper forumTopicIndexMapper;

    public ForumTopicServiceImpl(final ForumTopicRepository forumTopicRepository,
                                 final UserRepository userRepository,
                                 final ForumCategoryRepository forumCategoryRepository,
                                 final ForumTopicIndexingService forumTopicIndexingService,
                                 final ForumTopicSearchRepository forumTopicSearchRepository, ForumTopicIndexMapper forumTopicIndexMapper) {
        this.forumTopicRepository = forumTopicRepository;
        this.userRepository = userRepository;
        this.forumCategoryRepository = forumCategoryRepository;
        this.forumTopicIndexingService = forumTopicIndexingService;
        this.forumTopicSearchRepository = forumTopicSearchRepository;
        this.forumTopicIndexMapper = forumTopicIndexMapper;
    }

    @Override
    public Page<TopicDTO> getAllTopics(Long categoryId, Pageable pageable, UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

        return isAdmin ? forumTopicRepository.findByCategoryIdOrderByPinnedDesc(categoryId, pageable) : forumTopicRepository.findByCategoryIdAndHiddenFalseOrderByPinnedDesc(categoryId, pageable);
    }

    @Override
    public TopicDTO getForumTopicById(Long id) {
        return forumTopicRepository.findTopicDTOById(id)
                .orElseThrow(() -> new RuntimeException("Forum topic not found"));
    }

    @Override
    public ForumTopic createForumTopic(TopicDTO topic, UserDetails userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        ForumTopic newTopic = new ForumTopic();

        if (topic.isHidden() || topic.isPinned() || topic.isLocked()) {
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

            if (isAdmin) {
                newTopic.setHidden(topic.isHidden());
                newTopic.setPinned(topic.isPinned());
                newTopic.setLocked(topic.isLocked());
            } else {
                throw new RuntimeException("Unauthorized");
            }
        }

        ForumCategory category = forumCategoryRepository.findById(topic.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        newTopic.setCategory(category);
        newTopic.setCreatedBy(user);
        newTopic.setContent(topic.getContent());
        newTopic.setTitle(topic.getTitle());

        user.setForumPosts(user.getForumPosts() + 1);

        ForumTopic savedTopic = forumTopicRepository.save(newTopic);

        forumTopicIndexingService.indexForumTopic(savedTopic);

        return savedTopic;
    }

    @Override
    public ForumTopic updateForumTopic(Long id, TopicDTO topicDTO, UserDetails userDetails) {
        ForumTopic topic = forumTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum topic not found"));

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

        if (!isAdmin && !topic.getCreatedBy().getUserName().equals(userDetails.getUsername())) {
            throw new RuntimeException("Unauthorized to update this forum topic");
        }

        if (topic.isHidden() || topic.isPinned() || topic.isLocked()) {
            if (isAdmin) {
                topic.setHidden(topicDTO.isHidden());
                topic.setPinned(topicDTO.isPinned());
                topic.setLocked(topicDTO.isLocked());
            } else {
                throw new RuntimeException("Unauthorized");
            }
        }

//        ForumCategory category = forumCategoryRepository.findById(topicDTO.getCategoryId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));

        // topic.setCategory(category);
        topic.setTitle(topicDTO.getTitle());
        topic.setContent(topicDTO.getContent());

        forumTopicIndexingService.indexForumTopic(topic);
        return forumTopicRepository.save(topic);
    }

    @Override
    public void deleteForumTopic(Long id, UserDetails userDetails) {
        ForumTopic topic = forumTopicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum topic not found"));

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

        if (!isAdmin && !topic.getCreatedBy().getUserName().equals(userDetails.getUsername())) {
            throw new RuntimeException("Unauthorized to delete this forum topic");
        }

        ForumCategory category = topic.getCategory();
        category.getTopics().remove(topic);

        forumCategoryRepository.save(category);

        forumTopicIndexingService.removeIndexedTopic(id);
        forumTopicRepository.deleteById(id);
    }

    @Override
    public List<TopicIndexDTO> searchTopics(String keyword) {
        if (keyword == null || keyword.trim().length() < 3) {
            throw new RuntimeException("Search query must be at least 3 characters long");
        }
        return forumTopicSearchRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }
}
