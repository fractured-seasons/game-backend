package com.game.backend.services;

import com.game.backend.models.tickets.TicketComment;

import java.util.List;

public interface TicketCommentService {
    TicketComment addComment(Long ticketId, TicketComment comment, String username, boolean isAdmin);
    List<TicketComment> getCommentsByTicket(Long ticketId);
    void deleteComment(Long id);
}
