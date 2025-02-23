package com.game.backend.repositories.forums;

import com.game.backend.dtos.ReplyDTO;
import com.game.backend.models.forums.ForumReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ForumReplyRepository extends JpaRepository<ForumReply, Long> {
    Page<ForumReply> findByHiddenFalse(Pageable pageable);

    @Query("SELECT new com.game.backend.dtos.ReplyDTO(r.id, r.content, r.hidden, r.topic.id, r.lastUpdated, r.createdAt, r.createdBy) " +
            "FROM ForumReply r WHERE r.topic.id = :topicId ORDER BY r.createdAt ASC")
    Page<ReplyDTO> findByTopicId(Long topicId, Pageable pageable);

    @Query("SELECT new com.game.backend.dtos.ReplyDTO(r.id, r.content, r.hidden, r.topic.id, r.lastUpdated, r.createdAt, r.createdBy) " +
            "FROM ForumReply r WHERE r.hidden = false AND r.topic.id = :topicId ORDER BY r.createdAt ASC")
    Page<ReplyDTO> findByTopicIdAndHiddenFalse(Long topicId, Pageable pageable);
}
