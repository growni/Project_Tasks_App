package com.ipsos.controllers;

import com.ipsos.entities.Task;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.services.ProjectService;
import com.ipsos.services.TaskService;
import com.ipsos.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LEADER')")
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
    public String deleteTask(@RequestParam Long taskId, @RequestParam Long projectId) {
        this.taskService.deleteTask(taskId);

        return "redirect:/project/" + projectId;
    }

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.POST)
    public String addTask(@RequestParam Long projectId, @ModelAttribute TaskDto taskDto) {

        Task task = this.taskService.createTask(taskDto);
        this.projectService.addTask(projectId, task);

        return "redirect:/project/" + projectId;
    }


    @RequestMapping(value = "/project/editTask", method = RequestMethod.POST)
    public String editTask(@RequestParam Long taskId, @RequestParam Long projectId, @ModelAttribute TaskDto taskDto) {

        taskDto.setId(taskId);
        this.taskService.updateTask(taskDto);

        return "redirect:/project/" + projectId;
    }

    @RequestMapping(value = "/project/editProject", method = RequestMethod.POST)
    public String editProject(@RequestParam Long projectId, @ModelAttribute ProjectDto projectDto) {

        projectDto.setId(projectId);

        System.out.println(projectDto);
        this.projectService.updateProject(projectDto);

        return "redirect:/project/" + projectId;
    }

}
