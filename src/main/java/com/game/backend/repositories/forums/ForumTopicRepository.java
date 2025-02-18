package com.game.backend.repositories.forums;

import com.game.backend.models.forums.ForumTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, Long> {
}
