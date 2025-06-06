package com.game.backend.dtos.forum;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.backend.models.User;
import com.game.backend.serializers.UserSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReplyDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 1024, message = "Reply content must be between 3 and 1024 characters")
    private String content;

    private boolean hidden;

    @NotNull(message = "Topic ID is required")
    private Long topicId;

    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;

    @JsonSerialize(using = UserSerializer.class)
    private User createdBy;
}
