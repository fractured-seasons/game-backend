package com.game.backend.repositories.tickets;

import com.game.backend.dtos.TicketSummaryDTO;
import com.game.backend.models.tickets.Ticket;
import com.game.backend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Page<Ticket> findAllByCreatedBy(User createdBy, Pageable pageable);
    @Query("SELECT new com.game.backend.dtos.TicketSummaryDTO(t.id, t.title, t.status, t.lastUpdated, t.createdBy) " +
            "FROM Ticket t")
    Page<TicketSummaryDTO> findTicketSummaries(Pageable pageable);

    @Query("SELECT new com.game.backend.dtos.TicketSummaryDTO(t.id, t.title, t.status, t.lastUpdated, t.createdBy) " +
            "FROM Ticket t WHERE t.createdBy = :createdBy")
    Page<TicketSummaryDTO> findTicketSummariesByCreatedBy(Pageable pageable, User createdBy);
}
