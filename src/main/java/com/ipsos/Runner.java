package com.ipsos;

import com.ipsos.entities.Team;
import com.ipsos.entities.User;
import com.ipsos.entities.dtos.ProjectDto;
import com.ipsos.entities.dtos.UserDto;
import com.ipsos.repositories.TeamRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class Runner implements ApplicationRunner {

    private final UserService userService;
    private final ProjectService projectService;
    private final TeamService teamService;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public Runner(UserService userService, ProjectService projectService, TeamService teamService,
                  UserRepository userRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.projectService = projectService;
        this.teamService = teamService;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createEntities();
//        operations();
    }


    public void operations() {
        for (long i = 1; i < 5 ; i++) {
            for (long j = 1; j <=5; j++) {
                User user = this.userService.getById(i);
                Team team = this.teamService.getById(j);
                user.setTeam(this.teamService.getById(team.getId()));
                team.getMembers().add(user);

                this.teamRepository.save(team);
                this.userRepository.save(user);
            }


        }

    }
    public void createEntities() {


        String[] usernames = {"Aleksandar", "Maria", "Nova", "Inferno", "Zephyr", "Nebula", "Cinder",
                "Equinox", "Aether", "Vortex", "Thunder", "Oceanic", "Galaxy", "Drake", "Fusion", "Dusk", "Aurora", "Lagoon", "Kiril", "Adelina", "Galya", "Martina", "Martin"};

        for (String username : usernames) {
            UserDto userDto = new UserDto(username, "123123", "123123");
            this.userService.registerUser(userDto);
        }

        String[] projectNames = {
                "Alpha Project", "Beta Task", "Gamma Assignment", "Delta Operation", "Omega Venture",
                "Neon Undertaking", "Cosmic Mission", "Midnight Enterprise", "Emerald Initiative", "Blazing Assignment",
                "Golden Job", "Crimson Objective", "Silver Plan", "Arcane Campaign", "Electric Task",
                "Ruby Operation", "Icy Venture", "Astral Undertaking", "Azure Mission", "Mystic Project",
                "Shadow Enterprise", "Solar Initiative", "Lunar Task", "Fiery Objective", "Frostbite Plan"
        };

        for (String projectName : projectNames) {
            ProjectDto projectDto = new ProjectDto(projectName);
            this.projectService.createProject(projectDto);
        }

        String[] teamNames = {"Blaze", "Dragon", "Pokemons", "Lava lashers", "Tank soak"};

        for (String team : teamNames) {
            this.teamService.createTeam(team);
        }

    }
}
