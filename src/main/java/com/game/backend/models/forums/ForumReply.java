package com.game.backend.models.forums;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.game.backend.models.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString(exclude = "topic")
@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
public class ForumReply extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forum_reply_id")
    private Long id;

    @NotBlank
    @Column(length = 1024, nullable = false)
    private String content;

    private boolean hidden;

    @ManyToOne
    @JoinColumn(name = "forum_topic_id")
    @JsonBackReference
    private ForumTopic topic;
}
