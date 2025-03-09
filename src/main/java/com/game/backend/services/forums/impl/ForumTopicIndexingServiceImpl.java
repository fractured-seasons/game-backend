package com.game.backend.services.forums.impl;

import com.game.backend.models.forums.ForumTopic;
import com.game.backend.search.ForumTopicSearchRepository;
import com.game.backend.services.forums.ForumTopicIndexingService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ForumTopicIndexingServiceImpl implements ForumTopicIndexingService {
    private final ForumTopicSearchRepository forumTopicSearchRepository;

    public ForumTopicIndexingServiceImpl(final ForumTopicSearchRepository forumTopicSearchRepository) {
        this.forumTopicSearchRepository = forumTopicSearchRepository;
    }

    @Async
    public void indexForumTopic(ForumTopic topic) {
        forumTopicSearchRepository.save(topic);
    }

    @Async
    public void removeIndexedTopic(Long topicId) {
        forumTopicSearchRepository.deleteById(topicId);
    }
}
