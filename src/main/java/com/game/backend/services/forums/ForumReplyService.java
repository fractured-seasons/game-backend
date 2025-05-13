package com.game.backend.services.forums;

import com.game.backend.dtos.forum.ReplyDTO;
import com.game.backend.models.forums.ForumReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface ForumReplyService {
    ForumReply addForumReply(ReplyDTO replyDTO, UserDetails userDetails);

    Page<ReplyDTO> getAllReplies(Long topicId, Pageable pageable, UserDetails userDetails);

    ForumReply updateForumReply(Long id, ReplyDTO replyDTO, UserDetails userDetails);

    void deleteForumReply(Long id, UserDetails userDetails);
}
