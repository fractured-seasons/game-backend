package com.game.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryDTO {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 60)
    private String name;

    @Size(max = 256)
    private String description;

    @NotNull(message = "Section ID is required")
    private Long sectionId;
}
