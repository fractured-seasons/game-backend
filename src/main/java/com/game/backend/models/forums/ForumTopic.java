package com.game.backend.models.forums;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.game.backend.models.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ForumTopic extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forum_topic_id")
    private Long id;

    @NotBlank
    @Column(length = 60, nullable = false)
    private String title;

    @NotBlank
    @Column(length = 1024, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "forum_category_id")
    private ForumCategory category;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ForumReply> replies = new ArrayList<>();

    private boolean locked;
    private boolean pinned;
    private boolean hidden;
}
