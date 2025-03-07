package com.game.backend.repositories.forums;

import com.game.backend.dtos.forum.TopicDTO;
import com.game.backend.models.forums.ForumTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, Long> {
    @Query("SELECT new com.game.backend.dtos.forum.TopicDTO(t.id, t.title, t.content, t.category.id, t.locked, t.pinned, t.hidden, t.lastUpdated, t.createdAt, t.createdBy) " +
            "FROM ForumTopic t WHERE t.hidden = false AND t.category.id = :categoryId ORDER BY t.pinned DESC")
    Page<TopicDTO> findByCategoryIdAndHiddenFalseOrderByPinnedDesc(Long categoryId, Pageable pageable);

    @Query("SELECT new com.game.backend.dtos.forum.TopicDTO(t.id, t.title, t.content, t.category.id, t.locked, t.pinned, t.hidden, t.lastUpdated, t.createdAt, t.createdBy) " +
            "FROM ForumTopic t WHERE t.category.id = :categoryId ORDER BY t.pinned DESC")
    Page<TopicDTO> findByCategoryIdOrderByPinnedDesc(Long categoryId, Pageable pageable);

    @Query("SELECT new com.game.backend.dtos.forum.TopicDTO(t.id, t.title, t.content, t.category.id, t.locked, t.pinned, t.hidden, t.lastUpdated, t.createdAt, t.createdBy) " +
            "FROM ForumTopic t WHERE t.id = :topicId")
    Optional<TopicDTO> findTopicDTOById(Long topicId);
}
