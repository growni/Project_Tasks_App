package com.ipsos.services;

import com.ipsos.entities.Project;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.repositories.ProjectRepository;
import com.ipsos.repositories.TaskRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.Impl.ProjectServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    private ProjectService projectService;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);
        userService = mock(UserService.class);
        modelMapper = mock(ModelMapper.class);
        projectService = new ProjectServiceImpl(projectRepository, taskRepository, userRepository, userService, modelMapper);
    }

    @Test
    void testCreateProject() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Test Project");

        Project mappedProject = new Project();
        mappedProject.setName(projectDto.getName());
        mappedProject.setDueDate(projectDto.getDueDate());

        when(modelMapper.map(projectDto, Project.class)).thenReturn(mappedProject);
        when(projectRepository.save(mappedProject)).thenAnswer(invocation -> {
            Project savedProject = invocation.getArgument(0);
            savedProject.setId(1L);
            return savedProject;
        });
        Project createdProject = projectService.createProject(projectDto);

        assertNotNull(createdProject);
        assertEquals("Test Project", createdProject.getName());
        assertNotNull(createdProject.getDueDate());
    }


    @Test
    void testGetById() {
        long projectId = 1L;

        when(projectRepository.getProjectById(projectId)).thenReturn(Optional.of(new Project()));

        Project project = projectService.getById(projectId);
        assertNotNull(project);
    }

    @Test
    void testGetById_InvalidId() {
        long projectId = -1L;

        when(projectRepository.getProjectById(projectId)).thenReturn(Optional.of(new Project()));

        Project project = projectService.getById(projectId);
        assertNotNull(project);
    }

}
