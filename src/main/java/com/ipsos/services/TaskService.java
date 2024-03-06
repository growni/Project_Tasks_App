package com.ipsos.services;

import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;

public interface TaskService {
    Task createTask(Long projectId, TaskDto taskDto) throws IllegalAccessException;
    void deleteTask(Long projectId, Long taskId, Authentication authentication) throws IllegalAccessException;
    Task getById(Long taskId);
    void updateTask(Long projectId, TaskDto taskDto) throws IllegalAccessException;
}
