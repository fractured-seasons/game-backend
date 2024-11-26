package com.game.backend.services;

import com.game.backend.dtos.UserDTO;
import com.game.backend.models.User;
import com.game.backend.security.request.ForgotPasswordRequest;
import com.game.backend.security.request.PasswordResetRequest;
import com.game.backend.security.request.SignupRequest;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.security.response.LoginResponse;
import com.game.backend.security.response.LoginResponseJwtHeader;
import com.game.backend.security.response.UserDetailsResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

    LoginResponseJwtHeader authenticateUserJwtHeader(String username, String password);

    LoginResponse authenticateUser(String username, String password);

    ApiResponse registerUser(SignupRequest signUpRequest);

    ApiResponse sendPasswordResetEmail(ForgotPasswordRequest request);

    void resetPassword(PasswordResetRequest passwordResetRequest);

    UserDetailsResponse getUserDetails(String username, Collection<? extends GrantedAuthority> grantedAuthorities);
}
