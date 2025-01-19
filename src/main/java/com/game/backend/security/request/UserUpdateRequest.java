package com.game.backend.security.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Email(message = "Invalid email format")
    private String email;
    private String username;
}
