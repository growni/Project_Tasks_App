package com.ipsos.services.Impl;

import com.ipsos.entities.Project;
import com.ipsos.entities.Role;
import com.ipsos.entities.Team;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.exceptions.UsernameAlreadyExistsException;
import com.ipsos.repositories.ProjectRepository;
import com.ipsos.repositories.RoleRepository;
import com.ipsos.repositories.TeamRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ipsos.constants.ErrorMessages.AuthOperations.*;
import static com.ipsos.constants.ErrorMessages.TeamOperations.JOIN_REQUEST_ALREADY_SENT;
import static com.ipsos.constants.ErrorMessages.TeamOperations.TEAM_NOT_FOUND;
import static com.ipsos.constants.ErrorMessages.UserOperations.*;
import static com.ipsos.constants.Regex.PASSWORD_REGEX;
import static com.ipsos.constants.Regex.USERNAME_REGEX;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final RoleRepository roleRepository;
    private final TeamRepository teamRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, RoleRepository roleRepository, TeamRepository teamRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
        this.teamRepository = teamRepository;
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
            for (Role role : userDto.getRoles()) {
                validateRole(role);
            }
        }

        if(!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new InvalidDataException(CONFIRM_PASSWORD_INCORRECT);
        }

    }

    private void validateRole(Role role) {
        Set<String> ACCEPTED_ROLES = Set.of("ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_LEADER");

        if (!ACCEPTED_ROLES.contains(role.getName())) {
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
    public void updateUsername(Long userId, String username) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        Optional<User> byUsername = this.userRepository.getByUsername(username);

        if(byUsername.isPresent()) {
            throw new UsernameAlreadyExistsException(String.format(USER_EXISTS, username));
        }

        if(!isValidUsername(username)) {
            throw new InvalidDataException(INVALID_USERNAME);
        }

        user.setUsername(username);

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void updatePassword(Long userId, String password) {

        if(!isValidPassword(password)) {
            throw new InvalidDataException(INVALID_PASSWORD);
        }

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        if(passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidDataException(NEW_PASSWORD_IS_OLD_PASSWORD);
        }

        String hashedPassword = this.passwordEncoder.encode(password);
        user.setPassword(hashedPassword);

        this.userRepository.save(user);
    }

    @Override
    public void addRole(Long userId, String roleType) {
        Role role = new Role(roleType);

        validateRole(role);

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        Role roleFromDB = this.roleRepository.findByName(role.getName());

        if(roleFromDB != null) {
            user.getRoles().add(roleFromDB);
        } else {
            this.roleRepository.save(role);
            user.getRoles().add(role);
        }

        this.userRepository.save(user);
    }

    @Override
    public void removeRole(Long userId, String roleType) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        Role role = user.getRoles()
                .stream()
                .filter(r -> r.getName().equals(roleType))
                .findFirst()
                .orElseThrow(() ->
                        new EntityMissingFromDatabase(String.format(ROLE_NOT_ASSIGNED_TO, roleType, user.getUsername()))
                );


        user.getRoles().remove(role);
        this.userRepository.save(user);
    }

    @Override
    public void disableAccount(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        user.setEnabled(false);

        this.userRepository.save(user);
    }

    @Override
    public void activateAccount(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        user.setEnabled(true);

        this.userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void joinTeam(Long userId, Long teamId) throws IllegalAccessException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        boolean userHasTeam = user.getTeam() != null;

        if(userHasTeam) {
            throw new IllegalAccessException(String.format(USER_ALREADY_HAS_TEAM, user.getUsername(), user.getTeam().getName()));
        }

        Team team = this.teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityMissingFromDatabase(TEAM_NOT_FOUND));

        if(team.getJoinRequestUsernames().contains(user.getUsername())) {
            throw new UsernameAlreadyExistsException(String.format(JOIN_REQUEST_ALREADY_SENT, team.getName()));
        }

        team.getJoinRequestUsernames().add(user.getUsername());
        this.teamRepository.save(team);
    }

    @Override
    public boolean hasRole(Long userId, String roleType) {
        User user = getById(userId);

        return user.getRoles().stream().anyMatch(r -> r.getName().equals(roleType));
    }
}


