package com.game.backend.dtos;

import com.game.backend.models.User;
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
    private User createdBy;
}
