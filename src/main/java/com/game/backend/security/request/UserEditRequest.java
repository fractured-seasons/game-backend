package com.game.backend.security.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditRequest {
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    private String role;
    private Boolean enabled;
}
