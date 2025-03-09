package com.game.backend.search;

import com.game.backend.models.forums.ForumTopic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumTopicSearchRepository extends ElasticsearchRepository<ForumTopic, Long> {
    List<ForumTopic> findByTitleContainingIgnoreCase(String keyword);
}
