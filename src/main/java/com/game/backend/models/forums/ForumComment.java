package com.game.backend.models.forums;

import com.game.backend.models.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ForumComment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forum_comment_id")
    private Long id;

    @NotBlank
    @Column(length = 1024, nullable = false)
    private String content;

    private boolean hidden;
}
