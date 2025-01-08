package com.game.backend.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank
    String newPassword;
    @NotBlank
    String confirmNewPassword;
}
