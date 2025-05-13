package com.game.backend.controllers;

import com.game.backend.dtos.UserDTO;
import com.game.backend.models.User;
import com.game.backend.security.request.UserEditRequest;
import com.game.backend.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = userService.getAllUsers(pageable);

        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @PutMapping("/update-role")
    @PreAuthorize("@securityService.hasHigherRole(authentication, #userId, #roleName)")
    public ResponseEntity<String> updateUserRole(@RequestParam Long userId, 
                                                 @RequestParam String roleName) {
        userService.updateUserRole(userId, roleName);
        return ResponseEntity.ok("User role updated");
    }

    @PutMapping("/deactivate")
    @PreAuthorize("@securityService.hasHigherRole(authentication, #username)")
    public ResponseEntity<String> deactivateUser(@RequestParam String username) {
        try {
            userService.deactivateAccount(username);
            return ResponseEntity.ok("User disabled");
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PutMapping("/user/edit/{userId}")
    @PreAuthorize("@securityService.hasHigherRole(authentication, #userId)")
    public ResponseEntity<String> editUser(@PathVariable Long userId, @RequestBody UserEditRequest userEditRequest) {
        try {
            userService.editUserDetails(userId, userEditRequest);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/delete/{userId}")
    @PreAuthorize("@securityService.hasHigherRole(authentication, #userId)")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
