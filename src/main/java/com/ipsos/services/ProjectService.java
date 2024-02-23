package com.ipsos.services;

import com.ipsos.entities.Project;
import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);
    void updateJobNumber(Long projectId, String jobNumber);

    Project getById(Long id);

    void updateProject(ProjectDto projectDto);
    void updateDueDate(Long projectId, LocalDate date);
    void updateStatus(Long projectId, Status status);
    void updatePriority(Long projectId, Priority priority);
    void addTask(Long projectId, Task task);
   List<ProjectDto> getAllProjectsDto();
   ProjectDto getByIdDto(Long projectId);
   void assignUser(String username, Long projectId);
   void removeUser(String username, Long projectId);
   List<Project> findProjectByUsername(String username);


}
