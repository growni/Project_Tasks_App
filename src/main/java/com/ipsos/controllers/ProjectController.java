package com.ipsos.controllers;

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

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
    public String projectView(@PathVariable Long projectId, Model model) {

        ProjectDto projectDto = this.projectService.getByIdDto(projectId);
        List<User> users = this.userService.getAllUsers();

        model.addAttribute("users", users);
        model.addAttribute("project", projectDto);
        return "project";
    }



    @RequestMapping(value = "/project/assignUser", method = RequestMethod.POST)
    public String assignUser(@RequestParam Long projectId, @RequestParam String username) {

        this.projectService.assignUser(username, projectId);

        return "redirect:/project/" + projectId;
    }

    @RequestMapping(value = "/project/deleteTask", method = RequestMethod.POST)
    public String deleteTask(@RequestParam Long taskId, @RequestParam Long projectId) throws AccessDeniedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        this.taskService.deleteTask(projectId, taskId, authentication);

        return "redirect:/project/" + projectId;
    }

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.POST)
    public String addTask(@RequestParam Long projectId, @ModelAttribute TaskDto taskDto) throws AccessDeniedException {

        printUserRoles();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Task task = this.taskService.createTask(projectId, taskDto, authentication);
        this.projectService.addTask(projectId, task);

        return "redirect:/project/" + projectId;
    }


    @RequestMapping(value = "/project/editTask", method = RequestMethod.POST)
    public String editTask(@RequestParam Long taskId, @RequestParam Long projectId, @ModelAttribute TaskDto taskDto) throws AccessDeniedException {

        taskDto.setId(taskId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        this.taskService.updateTask(projectId, taskDto, authentication);

        return "redirect:/project/" + projectId;
    }

    @RequestMapping(value = "/project/editProject", method = RequestMethod.POST)
    public String editProject(@RequestParam Long projectId, @ModelAttribute ProjectDto projectDto) {

        projectDto.setId(projectId);

        System.out.println(projectDto);
        this.projectService.updateProject(projectDto);

        return "redirect:/project/" + projectId;
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
