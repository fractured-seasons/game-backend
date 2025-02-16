package com.game.backend.security.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String username;
    private List<String> roles;
    private boolean staff;
    public LoginResponse(String username, List<String> roles, boolean staff) {
        this.username = username;
        this.roles = roles;
        this.staff = staff;
    }
}

