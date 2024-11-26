package com.game.backend.controllers;

import com.game.backend.security.jwt.JwtUtils;
import com.game.backend.security.request.LoginRequest;
import com.game.backend.security.request.ForgotPasswordRequest;
import com.game.backend.security.request.PasswordResetRequest;
import com.game.backend.security.request.SignupRequest;
import com.game.backend.security.response.LoginResponse;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.security.response.UserDetailsResponse;
import com.game.backend.services.UserService;
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


    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetailsResponse response = userService.getUserDetails(userDetails.getUsername(), userDetails.getAuthorities());

        if (response != null) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.notFound().build();
        }
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

            LoginResponse checkAuthResponse = new LoginResponse(username, roles);
            return ResponseEntity.ok(checkAuthResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "Unauthorized"));
    }

}
