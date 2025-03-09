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
import lombok.Getter;

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

    //    @Lob
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(unique = true, nullable = false)
    private String slug;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wiki_category_id")
    @JsonBackReference
    private WikiCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approved_by")
    @JsonSerialize(using = UserSerializer.class)
    private User approvedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rejected_by")
    @JsonSerialize(using = UserSerializer.class)
    private User rejectedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hidden_by")
    @JsonSerialize(using = UserSerializer.class)
    private User hiddenBy;

    private boolean hidden;

    @PrePersist
    public void generateSlugOnCreate() {
        this.slug = generateSlugFromTitle(this.title);
    }

    @PreUpdate
    public void generateSlugOnUpdate() {
        if (this.title != null && !this.title.equals(getOriginalTitle())) {
            this.slug = generateSlugFromTitle(this.title);
        }
    }

    private String generateSlugFromTitle(String title) {
        return title.toLowerCase()
                .replace(" ", "-")
                .replaceAll("[^a-z0-9\\-]", "");
    }

    @Getter
    @Transient
    private String originalTitle;

    @PostLoad
    public void trackOriginalTitle() {
        this.originalTitle = this.title;
    }
}
