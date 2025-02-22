package com.game.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TopicDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 60)
    private String title;

    @NotBlank
    @Size(min = 3, max = 1024)
    private String content;

    @NotBlank
    private Long categoryId;

    private boolean locked;
    private boolean pinned;
    private boolean hidden;
}
