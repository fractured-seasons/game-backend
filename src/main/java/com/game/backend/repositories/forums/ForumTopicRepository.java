package com.game.backend.repositories.forums;

import com.game.backend.models.forums.ForumTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, Long> {
    Page<ForumTopic> findByCategoryIdAndHiddenFalseOrderByPinnedDesc(Long categoryId, Pageable pageable);
    Page<ForumTopic> findByCategoryIdOrderByPinnedDesc(Long categoryId, Pageable pageable);
}
