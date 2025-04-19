package com.game.backend.search;

import com.game.backend.dtos.forum.TopicIndexDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumTopicSearchRepository extends ElasticsearchRepository<TopicIndexDTO, Long> {
    List<TopicIndexDTO> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);
}
