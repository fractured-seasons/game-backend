package com.game.backend.controllers.forums;

import com.game.backend.models.forums.ForumTopic;
import com.game.backend.services.forums.ForumTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/forum/topic")
public class ForumTopicController {
    @Autowired
    ForumTopicService forumTopicService;

    @GetMapping()
    public ResponseEntity<Page<ForumTopic>> getAllTopics(@RequestParam Long categoryId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ForumTopic> topicsPage = forumTopicService.getAllTopics(categoryId, pageable, userDetails);

        return new ResponseEntity<>(topicsPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumTopic> getTopic(@PathVariable Long id) {
        return ResponseEntity.ok(forumTopicService.getForumTopicById(id));
    }
}
