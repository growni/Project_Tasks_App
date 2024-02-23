package com.ipsos;

import com.ipsos.entities.Project;
import com.ipsos.entities.Role;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.dtos.TaskDto;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.services.ProjectService;
import com.ipsos.services.RoleService;
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
    private final RoleService roleService;

    public Runner(UserService userService, ProjectService projectService, TaskService taskService, RoleService roleService) {
        this.userService = userService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.roleService = roleService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        createEntities();
//        operations();

    }


    public void operations() {
        Role ADMIN_ROLE = this.roleService.getByName("ROLE_ADMIN");
        Role DEVELOPER_ROLE = this.roleService.getByName("ROLE_DEVELOPER");
        Role TL_ROLE = this.roleService.getByName("ROLE_LEADER");


        User alex = this.userService.getById(1L);
        User kiro = this.userService.getById(2L);
        User adi = this.userService.getById(3L);
        User lucho = this.userService.getById(4L);
        User ico = this.userService.getById(5L);
        User mimeto = this.userService.getById(6L);
        User galeto = this.userService.getById(7L);
        User marto = this.userService.getById(8L);
        User marti = this.userService.getById(9L);
        User preska = this.userService.getById(10L);

        Project motleyProject = this.projectService.getById(1L);
        Project browniProject = this.projectService.getById(2L);
        Project lillyProject = this.projectService.getById(3L);
        Project pmiProject = this.projectService.getById(4L);
        Project laundryDetergentProject = this.projectService.getById(5L);
        Project babyDiaperProject = this.projectService.getById(6L);

//        Task dummyData = this.taskService.getById(1L);
//        Task MRT = this.taskService.getById(2L);
//        Task checkQA = this.taskService.getById(3L);
//        Task call = this.taskService.getById(4L);
//        Task waiting = this.taskService.getById(5L);
//        Task validationLogs = this.taskService.getById(6L);


        //this.projectService.assignUser("Aleksandar", 1L);
        //this.projectService.removeUser("Aleksandar", 1L);



    }
    public void createEntities() {
        UserDto userDto = new UserDto("Aleksandar", "123123");
        UserDto userDto2 = new UserDto("Kiril", "123123", "123123");
        UserDto userDto3 = new UserDto("Adelina", "123123", "123123");
        UserDto userDto4 = new UserDto("Lachezar", "123123", "123123");
        UserDto userDto5 = new UserDto("Hristo", "123123", "123123");
        UserDto userDto6 = new UserDto("Maria", "123123", "123123");
        UserDto userDto7 = new UserDto("Galya", "123123", "123123");
        UserDto userDto8 = new UserDto("Martin", "123123", "123123");
        UserDto userDto9 = new UserDto("Martina", "123123", "123123");
        UserDto userDto10 = new UserDto("Presyian", "123123", "123123");

        this.userService.registerUser(userDto);
        this.userService.registerUser(userDto2);
        this.userService.registerUser(userDto3);
        this.userService.registerUser(userDto4);
        this.userService.registerUser(userDto5);
        this.userService.registerUser(userDto6);
        this.userService.registerUser(userDto7);
        this.userService.registerUser(userDto8);
        this.userService.registerUser(userDto9);
        this.userService.registerUser(userDto10);

        ProjectDto projectDto = new ProjectDto("Motley");
        ProjectDto projectDto2 = new ProjectDto("Browni");
        ProjectDto projectDto3 = new ProjectDto("Lilly");
        ProjectDto projectDto4 = new ProjectDto("PMI Cluster 20");
        ProjectDto projectDto5 = new ProjectDto("Laundry Detergent");
        ProjectDto projectDto6 = new ProjectDto("Baby Diaper");

        this.projectService.createProject(projectDto);
        this.projectService.createProject(projectDto2);
        this.projectService.createProject(projectDto3);
        this.projectService.createProject(projectDto4);
        this.projectService.createProject(projectDto5);
        this.projectService.createProject(projectDto6);

        TaskDto taskDto = new TaskDto("Send dummy data");
        TaskDto taskDto2 = new TaskDto("Upload MRT");
        TaskDto taskDto3 = new TaskDto("Check with QA");
        TaskDto taskDto4 = new TaskDto("Go to a call with RE");
        TaskDto taskDto5 = new TaskDto("Waiting for updated materials");
        TaskDto taskDto6 = new TaskDto("Check logs from validation");
//


    }
}
