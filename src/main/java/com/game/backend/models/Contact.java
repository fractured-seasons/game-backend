package com.game.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Lob
    @NotBlank(message = "Message cannot be empty")
    private String message;
}