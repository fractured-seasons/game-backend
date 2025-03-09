package com.game.backend.controllers.forums;

import com.game.backend.dtos.forum.TopicDTO;
import com.game.backend.models.forums.ForumTopic;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.forums.ForumTopicService;
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

import java.util.List;

@RestController
@RequestMapping("api/forum/topic")
public class ForumTopicController {
    @Autowired
    ForumTopicService forumTopicService;

    @PostMapping("/create")
    public ResponseEntity<?> createForumTopic(@RequestBody @Valid TopicDTO topic, @AuthenticationPrincipal UserDetails userDetails) {
        ForumTopic createdTopic = forumTopicService.createForumTopic(topic, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTopic);
    }

    @GetMapping()
    public ResponseEntity<Page<TopicDTO>> getAllTopics(@RequestParam Long categoryId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TopicDTO> topicsPage = forumTopicService.getAllTopics(categoryId, pageable, userDetails);

        return new ResponseEntity<>(topicsPage, HttpStatus.OK);
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDTO> getTopic(@PathVariable Long topicId) {
        return ResponseEntity.ok(forumTopicService.getForumTopicById(topicId));
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<?> updateForumTopic(@PathVariable Long topicId, @RequestBody @Valid TopicDTO topicDTO, @AuthenticationPrincipal UserDetails userDetails) {
        ForumTopic updatedForumTopic = forumTopicService.updateForumTopic(topicId, topicDTO, userDetails);
        return ResponseEntity.ok(updatedForumTopic);
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<ApiResponse> deleteForumTopic(@PathVariable Long topicId, @AuthenticationPrincipal UserDetails userDetails) {
        forumTopicService.deleteForumTopic(topicId, userDetails);
        return ResponseEntity.ok(new ApiResponse(true, "Forum topic deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ForumTopic>> searchTopics(@RequestParam String query) {
        return ResponseEntity.ok(forumTopicService.searchTopics(query));
    }
}
