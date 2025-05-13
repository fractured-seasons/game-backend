package com.game.backend.dtos.wiki;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 60, message = "Article name must be between 3 and 60 characters")
    private String title;

    @NotBlank
    private String content;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
    private Long categoryTitle;
}
