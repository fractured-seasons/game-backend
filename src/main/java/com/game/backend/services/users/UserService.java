package com.game.backend.services.users;

import com.game.backend.dtos.UserDTO;
import com.game.backend.models.User;
import com.game.backend.security.request.*;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.security.response.LoginResponse;
import com.game.backend.security.response.LoginResponseJwtHeader;
import com.game.backend.security.response.UserDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    Page<User> getAllUsers(Pageable pageable);

    UserDTO getUserById(Long id);

    LoginResponseJwtHeader authenticateUserJwtHeader(String username, String email, String password);

    LoginResponse authenticateUser(String username, String password);

    ApiResponse registerUser(SignupRequest signUpRequest);

    ApiResponse sendPasswordResetEmail(ForgotPasswordRequest request);

    void resetPassword(PasswordResetRequest passwordResetRequest);

    UserDetailsResponse getUserDetails(String username, Collection<? extends GrantedAuthority> grantedAuthorities);

    Optional<User> findByEmail(String email);

    void registerUserOauth2(User user);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    ApiResponse updateUserDetails(String currentUsername, UserUpdateRequest updateRequest);

    String getUsername(String email);

    boolean deactivateAccount(String username);

    boolean deleteAccount(String username);

    boolean activateAccount(String username);

    void editUserDetails(Long id, UserEditRequest userEditRequest);

    void deleteUserById(Long id);

    User getUserByUserName(String name);
}
