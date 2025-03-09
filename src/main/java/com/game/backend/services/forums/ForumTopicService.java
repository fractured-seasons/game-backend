package com.game.backend.services.forums;

import com.game.backend.dtos.forum.TopicDTO;
import com.game.backend.models.forums.ForumTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ForumTopicService {
    Page<TopicDTO> getAllTopics(Long categoryId, Pageable pageable, UserDetails userDetails);

    TopicDTO getForumTopicById(Long id);

    ForumTopic createForumTopic(TopicDTO topic, UserDetails userDetails);

    ForumTopic updateForumTopic(Long id, TopicDTO topicDTO, UserDetails userDetails);

    void deleteForumTopic(Long id, UserDetails userDetails);
    List<ForumTopic> searchTopics(String keyword);
}
