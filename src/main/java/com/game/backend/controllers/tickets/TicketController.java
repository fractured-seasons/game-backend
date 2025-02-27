package com.game.backend.controllers.tickets;

import com.game.backend.dtos.TicketSummaryDTO;
import com.game.backend.models.tickets.Ticket;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.tickets.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket, @AuthenticationPrincipal UserDetails userDetails) {
        Ticket createdTicket = ticketService.createTicket(ticket, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPPORT', 'ROLE_MODERATOR', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TicketSummaryDTO>> getAllTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketSummaryDTO> ticketPage = ticketService.getAllTickets(pageable);

        return new ResponseEntity<>(ticketPage, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Page<TicketSummaryDTO>> getAllUserTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TicketSummaryDTO> ticketPage = ticketService.getAllUserTickets(pageable, userDetails.getUsername());

        return new ResponseEntity<>(ticketPage, HttpStatus.OK);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<?> updateTicket(@PathVariable Long ticketId, @RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.updateTicket(ticketId, ticket);
        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("/status/{ticketId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long ticketId, @RequestBody boolean status, @AuthenticationPrincipal UserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR") || auth.getAuthority().equals("ROLE_SUPPORT"));

        Ticket updatedTicket = ticketService.updateStatus(ticketId, status, isAdmin);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<ApiResponse> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok(new ApiResponse(true, "Ticket deleted successfully"));
    }
}
