package com.game.backend.services.impl;

import com.game.backend.dtos.UserDTO;
import com.game.backend.models.AppRole;
import com.game.backend.models.Role;
import com.game.backend.models.User;
import com.game.backend.repositories.RoleRepository;
import com.game.backend.repositories.UserRepository;
import com.game.backend.security.jwt.JwtUtils;
import com.game.backend.security.request.*;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.security.response.LoginResponse;
import com.game.backend.security.response.LoginResponseJwtHeader;
import com.game.backend.security.response.UserDetailsResponse;
import com.game.backend.security.services.EmailService;
import com.game.backend.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailService emailService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }


    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public LoginResponseJwtHeader authenticateUserJwtHeader(String username, String email, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername(), email, roles);

            return new LoginResponseJwtHeader(userDetails.getUsername(), roles, jwtToken);

        } catch (Exception exception) {
            throw new RuntimeException("Bad credentials", exception);
        }
    }

    @Override
    public LoginResponse authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return new LoginResponse(userDetails.getUsername(), roles);
        } catch (Exception exception) {
            throw new RuntimeException("Bad credentials", exception);
        }
    }


    @Override
    public ApiResponse registerUser(SignupRequest signUpRequest) {
        if (!signUpRequest.getUsername().matches("^[a-zA-Z0-9]+$")) {
            return new ApiResponse(false, "Username should only contain letters and numbers (no spaces or special characters).");
        }

        if (userRepository.existsByUserName(signUpRequest.getUsername())) {
            return new ApiResponse(false, "Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ApiResponse(false, "Email is already in use!");
        }

        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            return new ApiResponse(false, "Passwords should match.");
        }

        List<String> validationErrors = validatePassword(signUpRequest.getPassword());

        if (!validationErrors.isEmpty()) {
            String errorMessage = "Password should contain at least " + String.join(", ", validationErrors);
            return new ApiResponse(false, errorMessage);
        }

        User user = new User();
        user.setUserName(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Role role = getRoleForUser(strRoles);

        user.setRole(role);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");

        userRepository.save(user);

        return new ApiResponse(true, "User registered successfully!");
    }

    @Override
    public ApiResponse sendPasswordResetEmail(ForgotPasswordRequest request) {
        String email = request.getEmail();

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User with email " + email + " not found");
        }

        User user = userOptional.get();

        String resetToken = UUID.randomUUID().toString();

        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiryDate(System.currentTimeMillis() + 3600000);
        userRepository.save(user);

        String resetPasswordLink = frontendUrl + "/reset-password?token=" + resetToken;

        String subject = "Password Reset Request";
        String body = "To reset your password, click the link below:\n" + resetPasswordLink;
        emailService.sendEmail(user.getEmail(), subject, body);

        return new ApiResponse(true, "Password reset link has been sent to your email.");
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        Optional<User> userOptional = userRepository.findByPasswordResetToken(passwordResetRequest.getToken());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        User user = userOptional.get();

        if (System.currentTimeMillis() > user.getPasswordResetTokenExpiryDate()) {
            throw new RuntimeException("Reset token has expired");
        }

        if (!passwordResetRequest.getNewPassword().equals(passwordResetRequest.getConfirmNewPassword())) {
            throw new RuntimeException("New password and confirmation password do not match");
        }

        if (passwordEncoder.matches(passwordResetRequest.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the current password");
        }

        List<String> validationErrors = validatePassword(passwordResetRequest.getNewPassword());

        if (!validationErrors.isEmpty()) {
            String errorMessage = "Password should contain at least " + String.join(", ", validationErrors);
            throw new RuntimeException(errorMessage);
        }

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiryDate(null);
        userRepository.save(user);
    }

    private static List<String> validatePassword(String password) {
        List<String> validationErrors = new ArrayList<>();

        if (password.length() < 8) {
            validationErrors.add("8 characters");
        }

        if (!password.matches(".*[A-Z].*")) {
            validationErrors.add("one uppercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            validationErrors.add("one number");
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            validationErrors.add("one special character");
        }
        return validationErrors;
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findByUserName(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!currentUsername.equals(user.getUserName())) {
            throw new RuntimeException("You can only change your own password");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            throw new RuntimeException("New password and confirmation password do not match");
        }

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the current password");
        }

        List<String> validationErrors = validatePassword(changePasswordRequest.getNewPassword());

        if (!validationErrors.isEmpty()) {
            String errorMessage = "Password should contain at least " + String.join(", ", validationErrors);
            throw new RuntimeException(errorMessage);
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public ApiResponse updateUserDetails(String username, UserUpdateRequest updateRequest) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateRequest.getEmail() != null) {
            if (userRepository.existsByEmail(updateRequest.getEmail()) &&
                    !user.getEmail().equals(updateRequest.getEmail())) {
                throw new RuntimeException("Email is already in use");
            }
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getUsername() != null) {
            if (!updateRequest.getUsername().matches("^[a-zA-Z0-9]+$")) {
                throw new RuntimeException("Username should only contain letters and numbers (no spaces or special characters).");
            }
            user.setUserName(updateRequest.getUsername());
        }

        userRepository.save(user);

        return new ApiResponse(true, "User updated successfully");
    }

    @Override
    public void editUserDetails(Long userId, UserEditRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (updateRequest.getEmail() != null) {
            if (userRepository.existsByEmail(updateRequest.getEmail()) &&
                    !user.getEmail().equals(updateRequest.getEmail())) {
                throw new RuntimeException("Email is already in use");
            }
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getUsername() != null) {
            if (!updateRequest.getUsername().matches("^[a-zA-Z0-9]+$")) {
                throw new RuntimeException("Username should only contain letters and numbers (no spaces or special characters).");
            }
            user.setUserName(updateRequest.getUsername());
        }

        if (updateRequest.getRole() != null) {
            Role role = roleRepository.findByRoleName(AppRole.valueOf(updateRequest.getRole()))
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }

        if (updateRequest.getEnabled() != null) {
            user.setEnabled(updateRequest.getEnabled());
        }

        userRepository.save(user);
    }



    private Role getRoleForUser(Set<String> strRoles) {
        Role role;
        if (strRoles == null || strRoles.isEmpty()) {
            role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found."));
        } else {
            String roleStr = strRoles.iterator().next();
            if (roleStr.equals("admin")) {
                role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Role is not found."));
            } else {
                role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Role is not found."));
            }
        }
        return role;
    }

    @Override
    public UserDetailsResponse getUserDetails(String username, Collection<? extends GrantedAuthority> grantedAuthorities) {
        Optional<User> userOptional = userRepository.findByUserName(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            List<String> roles = grantedAuthorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            UserDetailsResponse response;
            response = modelMapper.map(user, UserDetailsResponse.class);
            response.setRoles(roles);

            return response;
        } else {
            return null;
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void registerUserOauth2(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
        userRepository.flush();
    }

    @Override
    public String getUsername(String email) {
        return userRepository.findByEmail(email)
                .map(User::getUserName)
                .orElse("");
    }

    public boolean deactivateAccount(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean activateAccount(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteAccount(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
