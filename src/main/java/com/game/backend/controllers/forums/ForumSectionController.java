package com.game.backend.controllers.forums;

import com.game.backend.models.forums.ForumSection;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.forums.ForumSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/forum/section")
public class ForumSectionController {
    @Autowired
    ForumSectionService forumSectionService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createSection(@RequestBody ForumSection forumSection, @AuthenticationPrincipal UserDetails userDetails) {
        ForumSection createdForumSection = forumSectionService.createForumSection(forumSection, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForumSection);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ForumSection> getSectionById(@PathVariable Long id) {
        return ResponseEntity.ok(forumSectionService.getForumSectionById(id));
    }

    @GetMapping
    public ResponseEntity<List<ForumSection>> getAllSections() {
        List<ForumSection> forumSections = forumSectionService.getAllForumSections();
        return new ResponseEntity<>(forumSections, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateSection(@PathVariable Long id, @RequestBody String updatedName) {
        ForumSection updatedForumSection = forumSectionService.updateForumSection(id, updatedName);
        return ResponseEntity.ok(updatedForumSection);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteSection(@PathVariable Long id) {
        forumSectionService.deleteSection(id);
        return ResponseEntity.ok(new ApiResponse(true, "Forum section deleted successfully"));
    }
}
