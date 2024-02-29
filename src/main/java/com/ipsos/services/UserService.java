package com.ipsos.services;


import com.ipsos.entities.Role;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;

import java.util.List;


public interface UserService {
    User registerUser(UserDto userDto);
    User getById(Long id);
    User getByUsername(String username);
    void addRoleToUser(String username, Role role);
    void deleteUser(Long userId);
    void updateUsername(Long userId, String username);
    void updatePassword(Long userId, String password);
    void addRole(Long userId, String roleType);

    boolean hasRole(Long userId, String roleType);
    void removeRole(Long userId, String roleType);
    void disableAccount(Long userId);
    void activateAccount(Long userId);
    List<User> getAllUsers();
    void joinTeam(Long userId, Long teamId) throws IllegalAccessException;

}
