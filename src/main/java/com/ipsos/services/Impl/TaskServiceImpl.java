package com.ipsos.services.Impl;

import com.ipsos.entities.BaseEntity;
import com.ipsos.entities.Project;
import com.ipsos.entities.Task;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.repositories.TaskRepository;
import com.ipsos.services.ProjectService;
import com.ipsos.services.TaskService;
import com.ipsos.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ipsos.constants.ErrorMessages.TaskOperations.TASK_DESCRIPTION_CANT_BE_NULL;
import static com.ipsos.constants.ErrorMessages.TaskOperations.TASK_NOT_FOUND;
import static com.ipsos.constants.ErrorMessages.TeamOperations.USER_NOT_LEADER_OF_THIS_TEAM;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectService projectService, UserService userService, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Task createTask(Long projectId, TaskDto taskDto) throws IllegalAccessException {

        validateUserAction(projectId);
        validateTaskDto(taskDto);

        Task task = this.modelMapper.map(taskDto, Task.class);

        return this.taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long projectId, Long taskId, Authentication authentication) throws IllegalAccessException {

        validateUserAction(projectId);

        Task task = getById(taskId);

        this.taskRepository.delete(task);
        this.taskRepository.flush();
    }

    @Override
    public void updateStatus(Long taskId, Status status) {
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityMissingFromDatabase(TASK_NOT_FOUND));

        task.setStatus(status);
        this.taskRepository.save(task);
    }

    @Override
    public void updatePriority(Long taskId, Priority priority) {
        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityMissingFromDatabase(TASK_NOT_FOUND));

        task.setPriority(priority);
        this.taskRepository.save(task);
    }

    private void validateTaskDto(TaskDto taskDto) {
        String description = taskDto.getDescription();

        if(description == null || description.trim().isEmpty() || description.length() > 250) {
            throw new InvalidDataException(TASK_DESCRIPTION_CANT_BE_NULL);
        }

    }

    @Override
    public Task getById(Long taskId) {
        Optional<Task> optionalTask = this.taskRepository.findById(taskId);

        if(optionalTask.isEmpty()) {
            throw new EntityMissingFromDatabase(TASK_NOT_FOUND);
        }

        return optionalTask.get();

    }

    @Override
    public void updateTask(Long projectId, TaskDto taskDto) throws IllegalAccessException {

        validateUserAction(projectId);
        validateTaskDto(taskDto);

        Task currentTask = getById(taskDto.getId());

        this.modelMapper.map(taskDto, currentTask);

        this.taskRepository.save(currentTask);
    }

    private void validateUserAction(Long projectId) throws IllegalAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        User loggedUser = this.userService.getByUsername(loggedUsername);

        Project project = this.projectService.getById(projectId);

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

        if(isLoggedUserCorrectTeamLeader) {
           return;
        }

        boolean isProjectAssignedToLoggedUser = loggedUser.getProjects().stream().map(BaseEntity::getId).anyMatch(id -> id.equals(projectId));

        if(!isProjectAssignedToLoggedUser) {
            throw new IllegalAccessException(String.format(USER_NOT_LEADER_OF_THIS_TEAM, loggedUsername, assignedUser.getUsername()));
        }

    }
}
