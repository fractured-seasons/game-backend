package com.game.backend.models.tickets;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.game.backend.models.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(exclude = "comments")
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Ticket extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @NotBlank
    @Column(length = 60, nullable = false)
    private String title;

    private boolean status;

    @NotBlank
    @Column(length = 400, nullable = false)
    private String postDetails;

    @Column(length = 512)
    private String mediaUrl;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<TicketComment> comments = new ArrayList<>();

    public boolean getStatus() {
        return status;
    }
}
