package com.game.backend.services.forums;

import com.game.backend.dtos.TopicDTO;
import com.game.backend.models.forums.ForumTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface ForumTopicService {
    Page<ForumTopic> getAllTopics(Long categoryId, Pageable pageable, UserDetails userDetails);

    ForumTopic getForumTopicById(Long id);

    ForumTopic createForumTopic(TopicDTO topic, UserDetails userDetails);

    ForumTopic updateForumTopic(Long id, TopicDTO topicDTO, UserDetails userDetails);

    void deleteForumTopic(Long id, UserDetails userDetails);
}
