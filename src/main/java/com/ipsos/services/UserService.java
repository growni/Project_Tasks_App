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
    void editUser(UserDto userDto);
    List<User> getAllUsers();

}
