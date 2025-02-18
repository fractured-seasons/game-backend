package com.game.backend.repositories.forums;

import com.game.backend.models.forums.ForumReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumReplyRepository extends JpaRepository<ForumReply, Long> {
}
