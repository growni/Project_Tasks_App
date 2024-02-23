package com.ipsos.services;

import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;

public interface TaskService {
    Task createTask(Long projectId, TaskDto taskDto, Authentication authentication) throws AccessDeniedException;
    void deleteTask(Long projectId, Long taskId, Authentication authentication) throws AccessDeniedException;
    void updateStatus(Long taskId, Status status);
    void updatePriority(Long taskId, Priority priority);
    Task getById(Long taskId);
    void updateTask(Long projectId, TaskDto taskDto, Authentication authentication) throws AccessDeniedException;
}
