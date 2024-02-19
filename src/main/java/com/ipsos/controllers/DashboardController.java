package com.ipsos.controllers;

import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.services.ProjectService;
import com.ipsos.services.TaskService;
import com.ipsos.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
public class DashboardController {

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;

    public DashboardController(ProjectService projectService, UserService userService, TaskService taskService) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskService = taskService;
    }


    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboardPage(Model model) {

        List<ProjectDto> projects = this.projectService.getAllProjectsDto();
        model.addAttribute("projects", projects);

        model.addAttribute("projectDto", new ProjectDto());

//        if(userDetails == null) {
//            return "redirect:/login";
//        }

        return "dashboard";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    public ModelAndView addProject(@ModelAttribute ProjectDto projectDto) {

        this.projectService.createProject(projectDto);

        ModelAndView view = new ModelAndView("redirect:/dashboard");
        return view;
    }


}
