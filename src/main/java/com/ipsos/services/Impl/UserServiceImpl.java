package com.ipsos.services.Impl;

import com.ipsos.entities.Project;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.exceptions.UserAlreadyAssignedException;
import com.ipsos.exceptions.UsernameAlreadyExistsException;
import com.ipsos.repositories.ProjectRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static com.ipsos.constants.ErrorMessages.AuthOperations.INVALID_PASSWORD;
import static com.ipsos.constants.ErrorMessages.AuthOperations.INVALID_USERNAME;
import static com.ipsos.constants.ErrorMessages.AuthOperations.USER_EXISTS;
import static com.ipsos.constants.ErrorMessages.ProjectOperations.*;
import static com.ipsos.constants.ErrorMessages.UserOperations.USERNAME_NOT_FOUND;
import static com.ipsos.constants.ErrorMessages.UserOperations.USER_NOT_FOUND;
import static com.ipsos.constants.Regex.PASSWORD_REGEX;
import static com.ipsos.constants.Regex.USERNAME_REGEX;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User getByUsername(String username) {
        User user = this.userRepository.getByUsername(username)
                .orElseThrow(() -> new EntityMissingFromDatabase(String.format(USERNAME_NOT_FOUND, username)));

        return user;
    }

    @Override
    @Transactional
    public void assignProject(Long userId, Long projectId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        Optional<Long> assignedProjectId = user.getProjects()
                .stream()
                .filter(p -> p.getId().equals(projectId))
                .map(Project::getId)
                .findFirst();

        if (assignedProjectId.isPresent()) {
            throw new UserAlreadyAssignedException(String.format(USER_ALREADY_ASSIGNED, project.getName(), user.getUsername()));
        }

        project.setUser(user);
        user.getProjects().add(project);

        this.projectRepository.save(project);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeProject(Long userId, Long projectId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        Optional<Long> assignedProjectId = user.getProjects()
                .stream()
                .filter(p -> p.getId().equals(projectId))
                .map(Project::getId)
                .findFirst();

        if(assignedProjectId.isEmpty()) {
            throw new EntityMissingFromDatabase(PROJECT_NOT_ASSIGNED_TO_USER);
        }

        user.getProjects().remove(project);
        project.setUser(null);

        this.userRepository.save(user);
        this.projectRepository.save(project);
    }

}


