package com.game.backend.controllers.forums;

import com.game.backend.dtos.CategoryDTO;
import com.game.backend.models.forums.ForumCategory;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.forums.ForumCategoryService;
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
@RequestMapping("api/forum/category")
public class ForumCategoryController {

    @Autowired
    ForumCategoryService forumCategoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createForumCategory(@RequestBody @Valid CategoryDTO forumCategory, @AuthenticationPrincipal UserDetails userDetails) {
        ForumCategory createdForumCategory = forumCategoryService.createForumCategory(forumCategory, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForumCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumCategory> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(forumCategoryService.getForumCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<ForumCategory>> getAllCategories() {
        List<ForumCategory> forumSections = forumCategoryService.getAllForumCategories();
        return new ResponseEntity<>(forumSections, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO updatedCategory) {
        ForumCategory updatedForumCategory = forumCategoryService.updateForumCategory(id, updatedCategory);
        return ResponseEntity.ok(updatedForumCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        forumCategoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse(true, "Forum category deleted successfully"));
    }
}
