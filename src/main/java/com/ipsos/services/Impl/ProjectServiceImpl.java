package com.ipsos.services.Impl;

import com.ipsos.entities.BaseEntity;
import com.ipsos.entities.Project;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.UserAlreadyAssignedException;
import com.ipsos.repositories.ProjectRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.ProjectService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static com.ipsos.constants.Errors.*;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Project createProject(ProjectDto projectDto) {

        Project project = this.modelMapper.map(projectDto, Project.class);

        this.projectRepository.save(project);

        return project;
    }

    @Override
    public void setJobNumber(Long projectId, String jobNumber) {

        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        project.setJobNumber(jobNumber);

        this.projectRepository.save(project);
    }

    @Override
    public void assignUser(Long userId, Long projectId) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));

        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        Optional<Long> isAlreadyAssigned = user.getProjects()
                .stream()
                .filter(p -> p.getId() == project.getId())
                .map(Project::getId).findFirst();

        if(isAlreadyAssigned.isPresent()) {
            throw new UserAlreadyAssignedException(USER_ALREADY_ASSIGNED);
        }

        project.setUser(user);
        this.projectRepository.save(project);
    }

    @Override
    public Project getById(Long id) {
        Optional<Project> optionalProject = this.projectRepository.getProjectById(id);

        if(optionalProject.isEmpty()) {
            throw new EntityMissingFromDatabase(PROJECT_NOT_FOUND);
        }

        return optionalProject.get();
    }

    @Override
    public void updateDueDate(Long projectId, LocalDate newDate) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        LocalDate currentDate = LocalDate.now();
        if(newDate.isBefore(currentDate)) {
            throw new IllegalArgumentException(NEW_DATE_MUST_BE_IN_FUTURE);
        }

        project.setDueDate(newDate);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void removeFromUser(Long projectId, Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityMissingFromDatabase(USER_NOT_FOUND));


        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        user.getProjects()
                .stream()
                .map(Project::getId)
                .filter(id -> id == project.getId())
                .findFirst()
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_ASSIGNED_TO_USER));

        user.getProjects().remove(project);
        this.userRepository.save(user);
        this.userRepository.flush();
    }
}
