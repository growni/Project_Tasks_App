package com.ipsos.controllers;

import com.ipsos.entities.Project;
import com.ipsos.entities.Task;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.services.ProjectService;
import com.ipsos.services.TaskService;
import com.ipsos.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/")
public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, TaskService taskService, UserService userService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping(value = "/project/{projectId}")
    public String projectView(@PathVariable Long projectId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();

        User loggedUser = this.userService.getByUsername(loggedUsername);

        boolean isUserLeader = this.userService.hasRole(loggedUser.getId(), "ROLE_LEADER");

        ProjectDto projectDto = this.projectService.getByIdDto(projectId);
        List<User> users = isUserLeader ? loggedUser.getTeam().getMembers()
                                        : this.userService.getAllUsers();

        model.addAttribute("users", users);
        model.addAttribute("project", projectDto);
        return "project";
    }



    @PostMapping(value = "/project/assignUser")
    public String assignUser(@RequestParam Long projectId, @RequestParam String username) throws IllegalAccessException {

        this.projectService.assignUser(username, projectId);

        return "redirect:/project/" + projectId;
    }

    @PostMapping(value = "/project/deleteTask")
    public String deleteTask(@RequestParam Long taskId, @RequestParam Long projectId) throws IllegalAccessException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        this.taskService.deleteTask(projectId, taskId, authentication);

        return "redirect:/project/" + projectId;
    }

    @PostMapping(value = "/project/{projectId}")
    public String addTask(@RequestParam Long projectId, @ModelAttribute TaskDto taskDto) throws IllegalAccessException {

        Task task = this.taskService.createTask(projectId, taskDto);
        this.projectService.addTask(projectId, task);

        return "redirect:/project/" + projectId;
    }


    @PostMapping(value = "/project/editTask")
    public String editTask(@RequestParam Long taskId, @RequestParam Long projectId, @ModelAttribute TaskDto taskDto) throws IllegalAccessException {

        taskDto.setId(taskId);

        this.taskService.updateTask(projectId, taskDto);

        return "redirect:/project/" + projectId;
    }

    @PostMapping(value = "/project/editProject")
    public String editProject(@RequestParam Long projectId, @ModelAttribute ProjectDto projectDto) throws IllegalAccessException {

        projectDto.setId(projectId);

        this.projectService.updateProject(projectDto);

        return "redirect:/project/" + projectId;
    }

    @PostMapping(value = "/project/delete")
    public String deleteProject(@RequestParam Long projectId) throws IllegalAccessException {
        this.projectService.deleteProject(projectId);

        return "redirect:/projects";
    }

    @GetMapping(value = "/projects")
    public String projectsView(Model model) {

        List<Project> projects = this.projectService.getAllProjects();

        model.addAttribute("projects", projects);

        return "projects";
    }

    public void printUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Username: " + authentication.getName());
            authentication.getAuthorities().forEach(authority -> {
                System.out.println("User has role: " + authority.getAuthority());
            });
        }
    }


}
