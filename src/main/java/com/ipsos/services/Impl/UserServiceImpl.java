package com.ipsos.services.Impl;

import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.exceptions.UsernameAlreadyExistsException;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.UserService;
import com.ipsos.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



import static com.ipsos.constants.Messages.*;
import static com.ipsos.constants.Regex.PASSWORD_REGEX;
import static com.ipsos.constants.Regex.USERNAME_REGEX;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtils validationUtils;


    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, ValidationUtils validationUtils) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.validationUtils = validationUtils;
    }

    @Override
    public User createUser(String username, String password) {

        if(!isValidUsername(username)) {
            throw new InvalidDataException(INVALID_USERNAME);
        }

        if(!isValidPassword(password)) {
            throw new InvalidDataException(INVALID_PASSWORD);
        }
        String hashedPassword = this.passwordEncoder.encode(password);

        UserDto userDto = new UserDto(username, hashedPassword);
        boolean isPresent = this.userRepository.getByUsername(username).isPresent();

        if(isPresent) {
            throw new UsernameAlreadyExistsException(String.format(USER_EXISTS, username));
        }

        User newUser = this.modelMapper.map(userDto, User.class);
        return this.userRepository.save(newUser);
    }

    public boolean isValidUsername(String username) {
        return username.trim() != "" && username.matches(USERNAME_REGEX);
    }

    public boolean isValidPassword(String password) {
        return password.trim() != "" && password.matches(PASSWORD_REGEX);
    }

}


