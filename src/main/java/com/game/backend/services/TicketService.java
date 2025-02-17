package com.game.backend.services;

import com.game.backend.dtos.TicketSummaryDTO;
import com.game.backend.models.tickets.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {
    Ticket createTicket(Ticket ticket, String username);
    Ticket getTicketById(Long id);
    Page<TicketSummaryDTO> getAllTickets(Pageable pageable);
    Ticket updateTicket(Long id, Ticket updatedTicket);
    void deleteTicket(Long id);
    Ticket updateStatus(Long id, boolean status, boolean isAdmin);

    Page<TicketSummaryDTO> getAllUserTickets(Pageable pageable, String username);
}
