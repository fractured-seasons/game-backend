package com.game.backend.services;

import com.game.backend.dtos.UserDTO;
import com.game.backend.models.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);
}
