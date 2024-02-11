package com.ipsos.services;

import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import org.springframework.stereotype.Service;


public interface UserService {
    User createUser(String username, String password);

}
