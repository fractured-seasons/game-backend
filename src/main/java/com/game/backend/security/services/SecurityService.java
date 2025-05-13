package com.game.backend.security.services;

import com.game.backend.dtos.UserDTO;
import com.game.backend.models.AppRole;
import com.game.backend.models.User;
import com.game.backend.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SecurityService {
    @Autowired
    private UserService userService;
    private final Map<AppRole, Integer> roleHierarchy = new HashMap<>();

    public SecurityService() {
        initializeRoleHierarchy();
    }

    private void initializeRoleHierarchy() {
        roleHierarchy.put(AppRole.ROLE_USER, 1);
        roleHierarchy.put(AppRole.ROLE_WIKI_CONTRIBUTOR, 2);
        roleHierarchy.put(AppRole.ROLE_SUPPORT, 3);
        roleHierarchy.put(AppRole.ROLE_MODERATOR, 4);
        roleHierarchy.put(AppRole.ROLE_ADMIN, 5);
    }

    public boolean hasHigherRole(Authentication authentication, Long targetUserId) {
        User currentUser = userService.getUserByUserName(authentication.getName());
        UserDTO targetUser = userService.getUserById(targetUserId);

        return isHigherRole(currentUser.getRole().getRoleName(), targetUser.getRole().getRoleName());
    }

    public boolean hasHigherRole(Authentication authentication, String targetUsername) {
        User currentUser = userService.getUserByUserName(authentication.getName());
        User targetUser = userService.getUserByUserName(targetUsername);

        return isHigherRole(currentUser.getRole().getRoleName(), targetUser.getRole().getRoleName());
    }

    public boolean hasHigherRole(Authentication authentication, Long targetUserId, String newRoleName) {
        User currentUser = userService.getUserByUserName(authentication.getName());
        UserDTO targetUser = userService.getUserById(targetUserId);

        AppRole newRole = AppRole.valueOf(newRoleName);
        return isHigherRole(currentUser.getRole().getRoleName(), targetUser.getRole().getRoleName()) && isHigherRole(currentUser.getRole().getRoleName(), newRole);
    }

    private boolean isHigherRole(AppRole currentRole, AppRole targetRole) {
        return roleHierarchy.get(currentRole) > roleHierarchy.get(targetRole);
    }
}
