package com.ipsos.controllers;

import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.services.ProjectService;
import com.ipsos.services.TaskService;
import com.ipsos.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @RequestMapping(value = "/dashboard/addProject", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LEADER')")
    public ModelAndView addProject(@ModelAttribute ProjectDto projectDto) {

        printUserRoles();

        this.projectService.createProject(projectDto);

        ModelAndView view = new ModelAndView("redirect:/dashboard");
        return view;
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
