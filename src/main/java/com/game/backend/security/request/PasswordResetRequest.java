package com.game.backend.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    @NotBlank
    String token;
    @NotBlank
    String newPassword;
    @NotBlank
    String confirmNewPassword;
}
