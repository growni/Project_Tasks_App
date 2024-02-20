package com.ipsos.services.Impl;

import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.repositories.TaskRepository;
import com.ipsos.services.TaskService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ipsos.constants.ErrorMessages.TaskOperations.TASK_DESCRIPTION_CANT_BE_NULL;
import static com.ipsos.constants.ErrorMessages.TaskOperations.TASK_NOT_FOUND;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Task createTask(TaskDto taskDto) {

        validateTaskDto(taskDto);

        Task task = this.modelMapper.map(taskDto, Task.class);

        return this.taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
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
    public void updateTask(TaskDto taskDto) {

        validateTaskDto(taskDto);

        Task currentTask = this.taskRepository.findById(taskDto.getId())
                .orElseThrow(() -> new EntityMissingFromDatabase(TASK_NOT_FOUND));

        this.modelMapper.map(taskDto, currentTask);

        this.taskRepository.save(currentTask);
    }
}
