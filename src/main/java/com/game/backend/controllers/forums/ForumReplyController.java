package com.game.backend.controllers.forums;

import com.game.backend.dtos.forum.ReplyDTO;
import com.game.backend.models.forums.ForumReply;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.forums.ForumReplyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/forum/reply")
public class ForumReplyController {
    @Autowired
    ForumReplyService forumReplyService;

    @PostMapping("/add")
    public ResponseEntity<?> addForumReply(@RequestBody @Valid ReplyDTO replyDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ForumReply newReply = forumReplyService.addForumReply(replyDTO, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReply);
    }

    @GetMapping()
    public ResponseEntity<Page<ReplyDTO>> getAllReplies(@RequestParam Long topicId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReplyDTO> repliesPage = forumReplyService.getAllReplies(topicId, pageable, userDetails);

        return new ResponseEntity<>(repliesPage, HttpStatus.OK);
    }

    @PutMapping("{topicId}")
    public ResponseEntity<?> updateForumReply(@PathVariable Long topicId, @RequestBody @Valid ReplyDTO replyDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ForumReply updatedForumReply = forumReplyService.updateForumReply(topicId, replyDTO, userDetails);
        return ResponseEntity.ok(updatedForumReply);
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<ApiResponse> deleteForumReply(@PathVariable Long topicId, @AuthenticationPrincipal UserDetails userDetails) {
        forumReplyService.deleteForumReply(topicId, userDetails);
        return ResponseEntity.ok(new ApiResponse(true, "Forum reply deleted successfully"));
    }
}
