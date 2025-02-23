package com.game.backend.controllers.forums;

import com.game.backend.dtos.ReplyDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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

    @PutMapping("{id}")
    public ResponseEntity<?> updateForumReply(@PathVariable Long id, @RequestBody @Valid ReplyDTO replyDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ForumReply updatedForumReply = forumReplyService.updateForumReply(id, replyDTO, userDetails);
        return ResponseEntity.ok(updatedForumReply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteForumReply(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        forumReplyService.deleteForumReply(id, userDetails);
        return ResponseEntity.ok(new ApiResponse(true, "Forum reply deleted successfully"));
    }
}
