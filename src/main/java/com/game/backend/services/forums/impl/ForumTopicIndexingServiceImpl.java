package com.game.backend.services.forums.impl;

import com.game.backend.dtos.forum.TopicIndexDTO;
import com.game.backend.mappers.ForumTopicIndexMapper;
import com.game.backend.models.forums.ForumTopic;
import com.game.backend.search.ForumTopicSearchRepository;
import com.game.backend.services.forums.ForumTopicIndexingService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ForumTopicIndexingServiceImpl implements ForumTopicIndexingService {
    private final ForumTopicSearchRepository forumTopicSearchRepository;
    private final ForumTopicIndexMapper forumTopicIndexMapper;

    public ForumTopicIndexingServiceImpl(final ForumTopicSearchRepository forumTopicSearchRepository, final ForumTopicIndexMapper forumTopicIndexMapper) {
        this.forumTopicSearchRepository = forumTopicSearchRepository;
        this.forumTopicIndexMapper = forumTopicIndexMapper;
    }

    @Async
    public void indexForumTopic(ForumTopic topic) {
        System.out.println("[START] Thread: " + Thread.currentThread().getName() + " - Topic: " + topic.getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        TopicIndexDTO indexDto = forumTopicIndexMapper.toIndexDTO(topic);
        forumTopicSearchRepository.save(indexDto);
        System.out.println("[END] Thread: " + Thread.currentThread().getName() + " - Topic: " + topic.getId());

    }

    @Async
    public void removeIndexedTopic(Long topicId) {
        forumTopicSearchRepository.deleteById(topicId);
    }
}
