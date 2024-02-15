package com.ipsos.services;

import com.ipsos.entities.Project;
import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;

import java.time.LocalDate;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);
    void updateJobNumber(Long projectId, String jobNumber);

    Project getById(Long id);
    void updateDueDate(Long projectId, LocalDate date);
    void updateStatus(Long projectId, Status status);
    void updatePriority(Long projectId, Priority priority);
    void addTask(Long projectId, Task task);
   // void removeTask(Long projectId, Task task);


}
