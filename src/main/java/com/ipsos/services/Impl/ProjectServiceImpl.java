package com.ipsos.services.Impl;

import com.ipsos.entities.Project;
import com.ipsos.entities.Task;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.repositories.ProjectRepository;
import com.ipsos.repositories.TaskRepository;
import com.ipsos.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.ipsos.constants.ErrorMessages.GenericOperations.DATE_MUST_BE_IN_FUTURE;
import static com.ipsos.constants.ErrorMessages.ProjectOperations.PROJECT_NOT_FOUND;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Project createProject(ProjectDto projectDto) {

        Project project = this.modelMapper.map(projectDto, Project.class);

        this.projectRepository.save(project);
        return project;
    }

    @Override
    public void updateJobNumber(Long projectId, String jobNumber) {

        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        project.setJobNumber(jobNumber);

        this.projectRepository.save(project);
    }

    @Override
    public Project getById(Long id) {
        Project project = this.projectRepository.getProjectById(id)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));


        return project;
    }

    @Override
    public void updateDueDate(Long projectId, LocalDate newDate) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        LocalDate currentDate = LocalDate.now();
        if(newDate.isBefore(currentDate)) {
            throw new IllegalArgumentException(DATE_MUST_BE_IN_FUTURE);
        }

        project.setDueDate(newDate);
        projectRepository.save(project);
    }

    @Override
    public void updateStatus(Long projectId, Status status) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        project.setStatus(status);
        this.projectRepository.save(project);
    }

    @Override
    public void updatePriority(Long projectId, Priority priority) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        project.setPriority(priority);
        this.projectRepository.save(project);
    }

    @Override
    public void addTask(Long projectId, Task task) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityMissingFromDatabase(PROJECT_NOT_FOUND));

        task.setProject(project);
        project.getTasks().add(task);

        this.taskRepository.save(task);
        this.projectRepository.save(project);
    }

}
