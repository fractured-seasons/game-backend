package com.game.backend.services.forums.impl;

import com.game.backend.dtos.ReplyDTO;
import com.game.backend.models.User;
import com.game.backend.models.forums.ForumReply;
import com.game.backend.models.forums.ForumTopic;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.forums.ForumReplyRepository;
import com.game.backend.repositories.forums.ForumTopicRepository;
import com.game.backend.services.forums.ForumReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ForumReplyServiceImpl implements ForumReplyService {
    @Autowired
    ForumReplyRepository forumReplyRepository;

    @Autowired
    ForumTopicRepository forumTopicRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ForumReply addForumReply(ReplyDTO replyDTO, UserDetails userDetails) {
        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        ForumTopic topic = forumTopicRepository.findById(replyDTO.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        if (topic.isLocked()) {
            throw new RuntimeException("You can't reply on a locked topic");
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

        if (!isAdmin && topic.isHidden()) {
            throw new RuntimeException("You can't reply on a hidden topic");
        }

        if (!isAdmin && replyDTO.isHidden()) {
            throw new RuntimeException("Unauthorized");
        }

        ForumReply newReply = new ForumReply();
        newReply.setContent(replyDTO.getContent());
        newReply.setHidden(replyDTO.isHidden());
        newReply.setCreatedBy(user);
        newReply.setTopic(topic);

        return forumReplyRepository.save(newReply);
    }

    @Override
    public Page<ReplyDTO> getAllReplies(Long topicId, Pageable pageable, UserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

        return isAdmin ? forumReplyRepository.findByTopicId(topicId, pageable) : forumReplyRepository.findByTopicIdAndHiddenFalse(topicId, pageable);

    }

    @Override
    public ForumReply updateForumReply(Long id, ReplyDTO replyDTO, UserDetails userDetails) {
        ForumReply reply = forumReplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reply not found"));

        User user = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        ForumTopic topic = forumTopicRepository.findById(replyDTO.getTopicId())
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        if (topic.isLocked()) {
            throw new RuntimeException("You can't update a reply on a locked topic");
        }

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

        if (!isAdmin && topic.isHidden()) {
            throw new RuntimeException("You can't update a reply on a hidden topic");
        }

        if (!isAdmin && replyDTO.isHidden()) {
            throw new RuntimeException("Unauthorized");
        }

        reply.setContent(replyDTO.getContent());
        reply.setHidden(replyDTO.isHidden());

        return forumReplyRepository.save(reply);
    }

    @Override
    @Transactional
    public void deleteForumReply(Long id, UserDetails userDetails) {
        ForumReply reply = forumReplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forum reply not found"));

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"));

        if (!isAdmin && !reply.getCreatedBy().getUserName().equals(userDetails.getUsername())) {
            throw new RuntimeException("Unauthorized to delete this forum reply");
        }

        ForumTopic topic = reply.getTopic();
        topic.getReplies().remove(reply);

        forumTopicRepository.save(topic);

        forumReplyRepository.delete(reply);
    }
}
