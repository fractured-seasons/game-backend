package com.game.backend.models.wiki;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.game.backend.models.User;
import com.game.backend.serializers.UserSerializer;
import com.game.backend.serializers.WikiArticleSerializer;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
public class WikiContribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wiki_contribution_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contributed_by", nullable = false)
    @JsonSerialize(using = UserSerializer.class)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "wiki_article_id", nullable = false)
    @JsonSerialize(using = WikiArticleSerializer.class)
    private WikiArticle wikiArticle;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
