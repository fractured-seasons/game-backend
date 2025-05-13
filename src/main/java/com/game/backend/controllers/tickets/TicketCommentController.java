package com.game.backend.controllers.tickets;

import com.game.backend.models.tickets.TicketComment;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.tickets.TicketCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class TicketCommentController {

    @Autowired
    private TicketCommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestParam Long ticketId,
                                        @RequestBody TicketComment comment,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR") || auth.getAuthority().equals("ROLE_SUPPORT"));

        TicketComment newComment = commentService.addComment(ticketId, comment, userDetails.getUsername(), isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<TicketComment>> getCommentsByTicket(@PathVariable Long ticketId) {
        return ResponseEntity.ok(commentService.getCommentsByTicket(ticketId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MODERATOR', 'ROLE_ADMIN')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new ApiResponse(true, "Comment deleted successfully"));
    }
}
