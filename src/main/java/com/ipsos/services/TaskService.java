package com.ipsos.services;

import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;

public interface TaskService {
    Task createTask(TaskDto taskDto);
    void deleteTask(Long taskId);
    void updateStatus(Long taskId, Status status);
    void updatePriority(Long taskId, Priority priority);
    Task getById(Long taskId);
    void updateTask(TaskDto taskDto);
}
