package com.ipsos.services;

import com.ipsos.entities.Project;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {
    User createUser(UserDto userDto);
    User getById(Long id);
    User getByUsername(String username);
    void assignProject(Long userId, Long projectId);
    void removeProject(Long userId, Long projectId);

}
