package com.game.backend.models.wiki;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.backend.models.Auditable;
import com.game.backend.models.User;
import com.game.backend.serializers.UserSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class WikiArticle extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wiki_article_id")
    private Long id;

    @NotBlank
    @Column(length = 60, nullable = false)
    private String title;

    @Lob
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(unique = true, nullable = false)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "wiki_category_id")
    @JsonBackReference
    private WikiCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    @JsonSerialize(using = UserSerializer.class)
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_by")
    @JsonSerialize(using = UserSerializer.class)
    private User rejectedBy;

    private boolean hidden;

    @PrePersist
    @PreUpdate
    public void generateSlug() {
        this.slug = title.toLowerCase().replace(" ", "-").replaceAll("[^a-z0-9\\-]", "");
    }
}
