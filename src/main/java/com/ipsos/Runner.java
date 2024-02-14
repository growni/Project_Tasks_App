package com.ipsos;

import com.ipsos.entities.Project;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.services.ProjectService;
import com.ipsos.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Scanner;


@Component
public class Runner implements ApplicationRunner {

    private final UserService userService;
    private final ProjectService projectService;

    public Runner(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createTestProject();
    }


    public void createTestProject() {
        //Scanner scanner = new Scanner(System.in);

//        UserDto userDto = new UserDto("Alex", "Test_Password");
//        this.userService.createUser(userDto);
//        System.out.println("Created user Alex");
//
//
       // ProjectDto projectDto = new ProjectDto("Motley");
//
      //  this.projectService.createProject(projectDto);
//        System.out.println("Created project Browni");
//
//        project.setJobNumber("(12-12312-124-01)");

//        this.projectService.updateDueDate(1L, LocalDate.parse("2025-12-12"));
//        this.projectService.setJobNumber(1L, "test jbnumer");
//        this.projectService.assignUser(user.getId(), project.getId());
//        System.out.println("Project Browni assigned to Alex.");
        Project project = this.projectService.getById(2L);
        User user = this.userService.getById(1L);

          this.projectService.removeFromUser(1L, 1L);

    }
}
