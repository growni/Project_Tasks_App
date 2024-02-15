package com.ipsos;

import com.ipsos.entities.Project;
import com.ipsos.entities.Task;
import com.ipsos.entities.User;
import com.ipsos.services.ProjectService;
import com.ipsos.services.TaskService;
import com.ipsos.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class Runner implements ApplicationRunner {

    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;

    public Runner(UserService userService, ProjectService projectService, TaskService taskService) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createTestProject();
    }


    public void createTestProject() {
        //Scanner scanner = new Scanner(System.in);

//        UserDto userDto = new UserDto("Alex", "Test_Password");
//        UserDto userDto2 = new UserDto("Kiro", "Test_Password");
//        this.userService.createUser(userDto);
//        this.userService.createUser(userDto2);
//
//        ProjectDto projectDto = new ProjectDto("Motley");
//        ProjectDto projectDto2 = new ProjectDto("Browni");
//
//        this.projectService.createProject(projectDto);
//        this.projectService.createProject(projectDto2);
//
//        TaskDto taskDto = new TaskDto("Send dummy data");
//        TaskDto taskDto2 = new TaskDto("Upload MRT");
//        this.taskService.createTask(taskDto);
//        this.taskService.createTask(taskDto2);
////
        User alex = this.userService.getById(1L);
        User kiro = this.userService.getById(2L);
        Project motleyProject = this.projectService.getById(1L);
        Project browniProject = this.projectService.getById(2L);

        Task dummyData = this.taskService.getById(1L);
        Task MRT = this.taskService.getById(2L);
//
        this.projectService.addTask(motleyProject.getId(), dummyData);
        this.projectService.addTask(browniProject.getId(), dummyData);
        this.taskService.deleteTask(dummyData.getId());

    }
}
