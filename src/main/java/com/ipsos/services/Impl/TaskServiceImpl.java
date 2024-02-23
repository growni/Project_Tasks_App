package com.ipsos.services.Impl;

import com.ipsos.entities.Project;
import com.ipsos.entities.Role;
import com.ipsos.entities.Task;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.repositories.TaskRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.TaskService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static com.ipsos.constants.ErrorMessages.AuthOperations.ACTION_NOT_ALLOWED;
import static com.ipsos.constants.ErrorMessages.TaskOperations.TASK_DESCRIPTION_CANT_BE_NULL;
import static com.ipsos.constants.ErrorMessages.TaskOperations.TASK_NOT_FOUND;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Task createTask(Long projectId, TaskDto taskDto, Authentication authentication) throws AccessDeniedException {

        authenticateUserAction(projectId, authentication);

        validateTaskDto(taskDto);

        Task task = this.modelMapper.map(taskDto, Task.class);

        return this.taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long projectId, Long taskId, Authentication authentication) throws AccessDeniedException {

        authenticateUserAction(projectId, authentication);

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityMissingFromDatabase(TASK_NOT_FOUND));

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
    public void updateTask(Long projectId, TaskDto taskDto, Authentication authentication) throws AccessDeniedException {

        authenticateUserAction(projectId, authentication);

        validateTaskDto(taskDto);

        Task currentTask = this.taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new EntityMissingFromDatabase(TASK_NOT_FOUND));

        this.modelMapper.map(taskDto, currentTask);

        this.taskRepository.save(currentTask);
    }

    private void authenticateUserAction(Long projectId, Authentication authentication) throws AccessDeniedException {
        if(authentication == null) {
            throw new AccessDeniedException(ACTION_NOT_ALLOWED);
        }

        String username = authentication.getName();
        User user = this.userRepository.getByUsername(username).get();

        Optional<Long> userProjectId = user.getProjects()
                .stream()
                .map(Project::getId)
                .filter(id -> id.equals(projectId))
                .findFirst();

        List<String> userRoles = user.getRoles()
                .stream()
                .filter(r ->
                        (r.getName().equals("ROLE_ADMIN") || r.getName().equals("ROLE_LEADER"))
                ).map(Role::getName)
                .toList();

        if(userProjectId.isEmpty() && userRoles.size() == 0) {
            throw new AccessDeniedException(ACTION_NOT_ALLOWED);
        }
    }
}
