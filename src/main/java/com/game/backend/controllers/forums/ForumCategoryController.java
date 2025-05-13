package com.game.backend.controllers.forums;

import com.game.backend.dtos.forum.CategoryDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/forum/category")
public class ForumCategoryController {

    @Autowired
    ForumCategoryService forumCategoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createForumCategory(@RequestBody @Valid CategoryDTO forumCategory, @AuthenticationPrincipal UserDetails userDetails) {
        ForumCategory createdForumCategory = forumCategoryService.createForumCategory(forumCategory, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdForumCategory);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ForumCategory> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(forumCategoryService.getForumCategoryById(categoryId));
    }

    @GetMapping
    public ResponseEntity<List<ForumCategory>> getAllCategories() {
        List<ForumCategory> forumCategories = forumCategoryService.getAllForumCategories();
        return new ResponseEntity<>(forumCategories, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryDTO updatedCategory) {
        ForumCategory updatedForumCategory = forumCategoryService.updateForumCategory(categoryId, updatedCategory);
        return ResponseEntity.ok(updatedForumCategory);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        forumCategoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse(true, "Forum category deleted successfully"));
    }
}
