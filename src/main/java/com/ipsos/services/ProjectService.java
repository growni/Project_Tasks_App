package com.ipsos.services;

import com.ipsos.entities.Project;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;

import java.time.LocalDate;

public interface ProjectService {
    Project createProject(ProjectDto projectDto);
    void setJobNumber(Long projectId, String jobNumber);
    void assignUser(Long userId, Long projectId);

    Project getById(Long id);
    void updateDueDate(Long projectId, LocalDate date);
    void removeFromUser(Long projectId, Long userId);

}
