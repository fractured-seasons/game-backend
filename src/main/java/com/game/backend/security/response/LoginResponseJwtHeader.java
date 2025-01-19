package com.game.backend.security.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponseJwtHeader {
    private String jwtToken;
    private String username;
    private List<String> roles;

    public LoginResponseJwtHeader(String username, List<String> roles, String jwtToken) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }
}

