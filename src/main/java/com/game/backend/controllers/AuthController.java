package com.game.backend.controllers;

import com.game.backend.security.jwt.JwtUtils;
import com.game.backend.security.request.*;
import com.game.backend.security.response.LoginResponse;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.security.response.UserDetailsResponse;
import com.game.backend.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;

//    @PostMapping("/public/signin/jwt-header")
//    public ResponseEntity<?> authenticateUserJwtHeader(@RequestBody LoginRequest loginRequest) {
//        try {
//            LoginResponse response = userService.authenticateUser(
//                    loginRequest.getUsername(),
//                    loginRequest.getPassword()
//            );
//            return ResponseEntity.ok(response);
//        } catch (RuntimeException e) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body("Bad credentials");
//        }
//    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticateUser(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            );

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(loginRequest.getUsername(), response.getRoles());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Bad credentials"));
        }
    }


    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        ApiResponse response = userService.registerUser(signUpRequest);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest passwordResetRequest) {
        try {
            ApiResponse response = userService.sendPasswordResetEmail(passwordResetRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Email not found"));
        }
    }

    @PostMapping("/public/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        try {
            userService.resetPassword(passwordResetRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Password has been successfully reset"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(changePasswordRequest);
            return ResponseEntity.ok(new ApiResponse(true, "Password has been successfully changed"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/user/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest updateRequest,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String currentUsername = userDetails.getUsername();
            ApiResponse response = userService.updateUserDetails(currentUsername, updateRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetailsResponse response = userService.getUserDetails(userDetails.getUsername(), userDetails.getAuthorities());

        if (response != null) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/deactivate")
    public ResponseEntity<?> deactivateAccount(HttpServletRequest request) {
        String jwtToken = jwtUtils.getJwtFromCookies(request);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

            boolean isDeactivated = userService.deactivateAccount(username);

            if (isDeactivated) {
                return ResponseEntity.ok(new ApiResponse(true, "Account deactivated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Failed to deactivate account."));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized"));
    }

    @PostMapping("/user/activate")
    public ResponseEntity<?> activateAccount(HttpServletRequest request) {
        String jwtToken = jwtUtils.getJwtFromCookies(request);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

            boolean isDeactivated = userService.activateAccount(username);

            if (isDeactivated) {
                return ResponseEntity.ok(new ApiResponse(true, "Account activated successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Failed to activate account."));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized"));
    }

    @PostMapping("/user/delete")
    public ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        String jwtToken = jwtUtils.getJwtFromCookies(request);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwtToken);

            boolean isDeleted = userService.deleteAccount(username);

            if (isDeleted) {
                return ResponseEntity.ok(new ApiResponse(true, "Account deleted successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Failed to delete account."));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized"));
    }


    @GetMapping("/username")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return (userDetails != null) ? userDetails.getUsername() : "";
    }

    @PostMapping("/public/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        ResponseCookie cookie = jwtUtils.generateCleanJwtCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse(true, "Successfully logged out"));
    }

    @GetMapping("/public/check-auth")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        String jwtToken = jwtUtils.getJwtFromCookies(request);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
            List<String> roles = jwtUtils.getRolesFromJwtToken(jwtToken);

            List<String> staffRoles = Arrays.asList("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_SUPPORT");

            boolean isStaff = roles.stream().anyMatch(staffRoles::contains);

            LoginResponse checkAuthResponse = new LoginResponse(username, roles, isStaff);
            return ResponseEntity.ok(checkAuthResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized"));
    }

    @PostMapping("/public/refresh-token/username")
    public ResponseEntity<?> refreshTokenUsername(HttpServletRequest request) {
        String jwtToken = jwtUtils.getJwtFromCookies(request);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String email = jwtUtils.getEmailFromJwtToken(jwtToken);
            List<String> roles = jwtUtils.getRolesFromJwtToken(jwtToken);

            String username = userService.getUsername(email);
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(username, roles);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ApiResponse(true, "Token refreshed successfully."));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized"));
    }

    @PostMapping("/public/refresh-token/email")
    public ResponseEntity<?> refreshTokenEmail(HttpServletRequest request) {
        String jwtToken = jwtUtils.getJwtFromCookies(request);
        if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
            List<String> roles = jwtUtils.getRolesFromJwtToken(jwtToken);

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(username, roles);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new ApiResponse(true, "Token refreshed successfully."));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized"));
    }

}
