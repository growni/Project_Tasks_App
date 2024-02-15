package com.ipsos.services;

import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;

import java.time.LocalDate;

public interface TaskService {
    Task createTask(TaskDto taskDto);
    void deleteTask(Long taskId);
    void updateStatus(Long taskId, Status status);
    void updatePriority(Long taskId, Priority priority);
    void updateDueDate(Long taskId, LocalDate newDate);

    Task getById(Long taskId);
}
