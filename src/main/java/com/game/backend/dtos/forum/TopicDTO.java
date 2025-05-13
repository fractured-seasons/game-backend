package com.game.backend.dtos.forum;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.backend.models.User;
import com.game.backend.serializers.UserSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 60, message = "Topic title must be between 3 and 60 characters")
    private String title;

    @NotBlank
    @Size(min = 3, max = 2048, message = "Topic content must be between 3 and 2048 characters")
    private String content;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private boolean locked;
    private boolean pinned;
    private boolean hidden;

    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;

    @JsonSerialize(using = UserSerializer.class)
    private User createdBy;
}
