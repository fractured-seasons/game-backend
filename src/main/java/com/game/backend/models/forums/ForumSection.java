package com.game.backend.models.forums;

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
public class ForumSection extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forum_section_id")
    private Long id;

    @NotBlank
    @Column(length = 60, nullable = false)
    private String name;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ForumCategory> categories;
}
