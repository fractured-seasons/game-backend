package com.game.backend.dtos.updates;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpdateDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 60, message = "Update title must be between 3 and 60 characters")
    private String title;

    @NotBlank
    private String content;

    private LocalDateTime createdAt;
}
