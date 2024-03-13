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
    Project getById(Long id);
    void updateProject(ProjectDto projectDto) throws IllegalAccessException;
    void addTask(Long projectId, Task task) throws IllegalAccessException;
   List<ProjectDto> getAllProjectsDto();
   List<Project> getAllProjects();
   ProjectDto getByIdDto(Long projectId);
   void assignUser(String username, Long projectId) throws IllegalAccessException;
   void removeUser(String username, Long projectId);
   List<Project> findProjectByUsername(String username);
   void deleteProject(Long projectId) throws IllegalAccessException;
}
