package com.game.backend.services.tickets.impl;

import com.game.backend.models.tickets.TicketComment;
import com.game.backend.models.tickets.Ticket;
import com.game.backend.models.User;
import com.game.backend.repositories.tickets.TicketCommentRepository;
import com.game.backend.repositories.tickets.TicketRepository;
import com.game.backend.repositories.UserRepository;
import com.game.backend.services.tickets.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TicketCommentServiceImpl implements TicketCommentService {

    @Autowired
    private TicketCommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public TicketComment addComment(Long ticketId, TicketComment comment, String username, boolean isAdmin) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        if (!isAdmin && !Objects.equals(ticket.getCreatedBy().getUserName(), username)) {
            throw new RuntimeException("Unauthorized to comment on this ticket");
        }

        if (!ticket.getStatus()) {
            throw new RuntimeException("You can't reply on a closed ticket");
        }

        comment.setTicket(ticket);
        comment.setPostedBy(user);

        if (isAdmin && !Objects.equals(ticket.getCreatedBy().getUserName(), user.getUserName())) {
            comment.setCreatedByAdmin(true);
        }

        return commentRepository.save(comment);
    }

    public List<TicketComment> getCommentsByTicket(Long ticketId) {
        return commentRepository.findByTicketId(ticketId);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
