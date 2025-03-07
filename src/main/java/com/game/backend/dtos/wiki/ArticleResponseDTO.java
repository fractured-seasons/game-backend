package com.game.backend.dtos.wiki;

import com.game.backend.models.wiki.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDTO {
    private Long id;
    private String title;
    private String slug;
    private ApprovalStatus approvalStatus;
    private boolean hidden;
    private LocalDateTime createdAt;
    private String categoryTitle;
}
