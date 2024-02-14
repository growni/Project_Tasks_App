package com.ipsos.services.Impl;

import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.exceptions.UsernameAlreadyExistsException;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.UserService;
import com.ipsos.utils.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static com.ipsos.constants.Errors.INVALID_PASSWORD;
import static com.ipsos.constants.Errors.INVALID_USERNAME;
import static com.ipsos.constants.Errors.*;
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
    public User createUser(UserDto userDto) {

        validateUserDto(userDto);

        String hashedPassword = this.passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(hashedPassword);

        User newUser = this.modelMapper.map(userDto, User.class);
        return this.userRepository.save(newUser);
    }

    private void validateUserDto(UserDto userDto) {
        if(!isValidUsername(userDto.getUsername())) {
            throw new InvalidDataException(INVALID_USERNAME);
        }

        if(!isValidPassword(userDto.getPassword())) {
            throw new InvalidDataException(INVALID_PASSWORD);
        }

        boolean isPresent = this.userRepository.getByUsername(userDto.getUsername()).isPresent();

        if(isPresent) {
            throw new UsernameAlreadyExistsException(String.format(USER_EXISTS, userDto.getUsername()));
        }
    }

    public boolean isValidUsername(String username) {
        return username.trim() != "" && username.matches(USERNAME_REGEX);
    }

    public boolean isValidPassword(String password) {
        return password.trim() != "" && password.matches(PASSWORD_REGEX);
    }

    @Override
    public User getById(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);

        if(optionalUser.isEmpty()) {
            throw new EntityMissingFromDatabase(USER_NOT_FOUND);
        }

        return optionalUser.get();
    }




}


