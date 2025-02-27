package com.game.backend.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.backend.models.User;
import com.game.backend.serializers.UserSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketSummaryDTO {
    private Long id;
    private String title;
    private boolean status;
    private LocalDateTime lastUpdated;

    @JsonSerialize(using = UserSerializer.class)
    private User createdBy;
}
