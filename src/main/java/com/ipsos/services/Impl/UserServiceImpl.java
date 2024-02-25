package com.ipsos.services.Impl;

import com.ipsos.entities.Project;
import com.ipsos.entities.Role;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.exceptions.UsernameAlreadyExistsException;
import com.ipsos.repositories.ProjectRepository;
import com.ipsos.repositories.RoleRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ipsos.constants.ErrorMessages.AuthOperations.*;
import static com.ipsos.constants.ErrorMessages.UserOperations.*;
import static com.ipsos.constants.Regex.PASSWORD_REGEX;
import static com.ipsos.constants.Regex.USERNAME_REGEX;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserDto userDto) {

        validateUserDto(userDto);

        boolean isPresent = this.userRepository.getByUsername(userDto.getUsername()).isPresent();

        if(isPresent) {
            throw new UsernameAlreadyExistsException(String.format(USER_EXISTS, userDto.getUsername()));
        }

        String hashedPassword = this.passwordEncoder.encode(userDto.getPassword());

        userDto.setPassword(hashedPassword);
        userDto.setRoles(Set.of(roleRepository.findByName("ROLE_DEVELOPER")));
        userDto.setEnabled(false);

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

        if(userDto.getRoles().size() > 0) {
            validateRoles(userDto.getRoles());
        }

    }

    private void validateRoles(Set<Role> roles) {
        Set<String> ACCEPTED_ROLES = Set.of("ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_LEADER");

        boolean containsOnlyValidRoles = roles.stream().allMatch(ACCEPTED_ROLES::contains);

        if(!containsOnlyValidRoles) {
            throw new InvalidDataException(INVALID_USER_ROLE);
        }
    }

    public boolean isValidUsername(String username) {
        return !username.trim().equals("") && username.matches(USERNAME_REGEX);
    }

    public boolean isValidPassword(String password) {
        return !password.trim().equals("") && password.matches(PASSWORD_REGEX);
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
    public void addRoleToUser(String username, Role role) {
        User user = this.userRepository.getByUsername(username)
                .orElseThrow(() -> new EntityMissingFromDatabase(String.format(USERNAME_NOT_FOUND, username)));

        user.getRoles().add(role);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        List<Project> userProjects = user.getProjects();

        for (Project project : userProjects) {
            project.setUser(null);
        }

        this.projectRepository.saveAll(userProjects);
        this.userRepository.delete(user);
    }

    @Override
    @Transactional
    public void editUser(UserDto userDto) {
        validateUserDto(userDto);

        User currentUser = this.userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        this.modelMapper.map(userDto, currentUser);
        this.userRepository.save(currentUser);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }


}


