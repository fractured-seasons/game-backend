package com.game.backend.models.updates;

import com.game.backend.models.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Update extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "update_id")
    private Long id;

    @NotBlank
    @Column(length = 60, nullable = false)
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;
}
