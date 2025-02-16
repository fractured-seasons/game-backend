package com.game.backend.services.impl;

import com.game.backend.dtos.TicketSummaryDTO;
import com.game.backend.models.Ticket;
import com.game.backend.models.TicketComment;
import com.game.backend.models.User;
import com.game.backend.repositories.TicketCommentRepository;
import com.game.backend.repositories.TicketRepository;
import com.game.backend.repositories.UserRepository;
import com.game.backend.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketCommentRepository ticketCommentRepository;

    @Autowired
    private UserRepository userRepository;

    public Ticket createTicket(Ticket ticket, String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Your account is deactivated");
        }

        ticket.setCreatedBy(user);
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public Page<TicketSummaryDTO> getAllTickets(Pageable pageable) {
       return ticketRepository.findTicketSummaries(pageable);
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket ticket = getTicketById(id);
        ticket.setPostDetails(updatedTicket.getPostDetails());
        ticket.setMediaUrl(updatedTicket.getMediaUrl());
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Ticket updateStatus(Long id, boolean status, boolean isAdmin) {
        Ticket ticket = getTicketById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByUserName(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Objects.equals(ticket.getCreatedBy().getUserName(), currentUsername) && !isAdmin) {
            throw new RuntimeException("Unauthorized to update ticket status");
        }

        if (isAdmin && Objects.equals(ticket.getCreatedBy().getUserName(), currentUsername) && status) {
            throw new RuntimeException("Unauthorized to reopen your ticket status");
        }

        if (!ticket.getStatus() && !isAdmin) {
            throw new RuntimeException("Unauthorized to reopen this ticket");
        }

        if (ticket.getStatus() == status) {
            throw new RuntimeException("You can't put same status");
        }

        TicketComment ticketComment = new TicketComment();
        ticketComment.setText(status ? "Ticket reopened" : "Ticket marked as closed");
        ticketComment.setPostedBy(currentUser);
        ticketComment.setCreatedByAdmin(isAdmin);
        ticketComment.setTicket(ticket);

        ticketCommentRepository.save(ticketComment);

        ticket.setStatus(status);
        return  ticketRepository.save(ticket);
    }

    @Override
    public Page<TicketSummaryDTO> getAllUserTickets(Pageable pageable, String username) {
        User currentUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ticketRepository.findTicketSummariesByCreatedBy(pageable, currentUser);
    }
}
