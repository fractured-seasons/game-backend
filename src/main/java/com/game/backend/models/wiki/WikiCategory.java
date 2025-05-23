package com.game.backend.models.wiki;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.game.backend.models.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class WikiCategory extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wiki_category_id")
    private Long id;

    @NotBlank
    @Column(length = 60, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<WikiArticle> articles;
}
