package com.game.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SectionDTO {
    @NotBlank
    @Size(min = 3, max = 60, message = "Section name must be between 3 and 60 characters")
    private String name;

    private List<CategoryDTO> categories;
}
