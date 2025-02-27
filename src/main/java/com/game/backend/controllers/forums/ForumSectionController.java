package com.game.backend.controllers.forums;

import com.game.backend.dtos.SectionDTO;
import com.game.backend.models.forums.ForumSection;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.forums.ForumSectionService;
import jakarta.validation.Valid;
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
    @GetMapping("/{sectionId}")
    public ResponseEntity<ForumSection> getSectionById(@PathVariable Long sectionId) {
        return ResponseEntity.ok(forumSectionService.getForumSectionById(sectionId));
    }

    @GetMapping
    public ResponseEntity<List<ForumSection>> getAllSections() {
        List<ForumSection> forumSections = forumSectionService.getAllForumSections();
        return new ResponseEntity<>(forumSections, HttpStatus.OK);
    }

    @PutMapping("/{sectionId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateSection(@PathVariable Long sectionId, @RequestBody @Valid SectionDTO request) {
        ForumSection updatedForumSection = forumSectionService.updateForumSection(sectionId, request.getName());
        return ResponseEntity.ok(updatedForumSection);
    }

    @DeleteMapping("/{sectionId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteSection(@PathVariable Long sectionId) {
        forumSectionService.deleteSection(sectionId);
        return ResponseEntity.ok(new ApiResponse(true, "Forum section deleted successfully"));
    }
}
