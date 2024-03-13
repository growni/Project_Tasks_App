package com.ipsos.services.Impl;

import com.ipsos.entities.Project;
import com.ipsos.entities.Task;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.exceptions.UserAlreadyAssignedException;
import com.ipsos.repositories.ProjectRepository;
import com.ipsos.repositories.TaskRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.ProjectService;
import com.ipsos.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ipsos.constants.ErrorMessages.GenericOperations.DATE_MUST_BE_IN_FUTURE;
import static com.ipsos.constants.ErrorMessages.ProjectOperations.*;
import static com.ipsos.constants.ErrorMessages.TeamOperations.USER_NOT_LEADER_OF_THIS_TEAM;
import static com.ipsos.constants.Regex.NAME_REGEX;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository, UserService userService, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;

    }

    @Override
    public Project createProject(ProjectDto projectDto) {

        validateProjectDto(projectDto);

        Project project = this.modelMapper.map(projectDto, Project.class);

        this.projectRepository.save(project);
        return project;
    }

    private boolean validateProjectDto(ProjectDto projectDto) {
        String name = projectDto.getName();

        if(name.trim().isEmpty() || !name.matches(NAME_REGEX)) {
            throw new InvalidDataException(INVALID_PROJECT_NAME);
        }

        LocalDate dueDate = projectDto.getDueDate();
        LocalDate currentDate = LocalDate.now();

        if(dueDate == null) {
            dueDate = LocalDate.now();
        }

        if(dueDate.isBefore(currentDate)) {
            throw new IllegalArgumentException(DATE_MUST_BE_IN_FUTURE);
        }

        return true;

    }

    @Override
    public Project getById(Long id) {
        return this.projectRepository.getProjectById(id)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));
    }

    @Override
    public void updateProject(ProjectDto projectDto) throws IllegalAccessException {

        validateProjectDto(projectDto);
        validateUserAction(projectDto.getId());

        Project currentProject = getById(projectDto.getId());

        if(currentProject.getUser() != null) {
            projectDto.setUser(currentProject.getUser());
        }

        this.modelMapper.map(projectDto, currentProject);
        this.projectRepository.save(currentProject);
    }

    @Override
    public void addTask(Long projectId, Task task) {
        Project project = getById(projectId);

        project.getTasks().add(task);
        this.projectRepository.save(project);

        task.setProject(project);
        this.taskRepository.save(task);
    }

    @Override
    public List<ProjectDto> getAllProjectsDto() {
        List<Project> allProjects = this.projectRepository.findAll();

        List<ProjectDto> allProjectsDto = new ArrayList<>();
        for (Project project : allProjects) {
            ProjectDto dto = this.modelMapper.map(project, ProjectDto.class);
            allProjectsDto.add(dto);
        }

        return  allProjectsDto;

    }

    @Override
    public ProjectDto getByIdDto(Long projectId) {
        Project project = getById(projectId);

        ProjectDto projectDto = this.modelMapper.map(project, ProjectDto.class);

        return projectDto;
    }

    @Override
    @Transactional
    public void assignUser(String username, Long projectId) throws IllegalAccessException {
        User user = this.userService.getByUsername(username);

        Project project = getById(projectId);

        User assignedUser = project.getUser();

        if(user == assignedUser) {
            throw new UserAlreadyAssignedException(String.format(USER_ALREADY_ASSIGNED, project.getName(), user.getUsername()));
        }

        validateUserAction(projectId);

        project.setUser(user);
        user.getProjects().add(project);

        this.projectRepository.save(project);
        this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUser(String username, Long projectId) {
        User user = this.userService.getByUsername(username);

        Project project = getById(projectId);

        User assignedUser = project.getUser();

        if(assignedUser == null || !assignedUser.equals(user)) {
            throw new EntityMissingFromDatabase(PROJECT_NOT_ASSIGNED_TO_USER);
        }

        user.getProjects().remove(project);
        project.setUser(null);

        this.userRepository.save(user);
        this.projectRepository.save(project);
    }

    @Override
    public List<Project> findProjectByUsername(String username) {
        return this.projectRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) throws IllegalAccessException {
        Project project = getById(projectId);

        validateUserAction(projectId);

        this.projectRepository.delete(project);
    }

    private void validateUserAction(Long projectId) throws IllegalAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        User loggedUser = this.userService.getByUsername(loggedUsername);

        Project project = getById(projectId);

        User assignedUser = project.getUser();

        if(assignedUser == null) {
            return;
        }

        if(assignedUser.getTeam() == null) {
            return;
        }

        if(this.userService.hasRole(loggedUser.getId(), "ROLE_ADMIN")) {
            return;
        }

        boolean isLoggedUserCorrectTeamLeader = this.userService.hasRole(loggedUser.getId(), "ROLE_LEADER") && loggedUser.getTeam().getId().equals(assignedUser.getTeam().getId());

        if(!isLoggedUserCorrectTeamLeader) {
            throw new IllegalAccessException(String.format(USER_NOT_LEADER_OF_THIS_TEAM, loggedUsername, assignedUser.getUsername()));
        }

    }

    @Override
    public List<Project> getAllProjects() {
        return this.projectRepository.findAll();
    }
}
