package com.ipsos.controllers;

import com.ipsos.entities.Project;
import com.ipsos.entities.Team;
import com.ipsos.entities.User;
import com.ipsos.services.TeamService;
import com.ipsos.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    public TeamController(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping("/team/{teamId}")
    public String teamView(@PathVariable Long teamId, Model model) {

        Team team = this.teamService.getById(teamId);
        List<User> users = this.userService.getAllUsers();
        List<String> joinRequests = team.getJoinRequestUsernames();

        model.addAttribute("team", team);
        model.addAttribute("users", users);
        model.addAttribute("joinRequests", joinRequests);

        return "team";
    }

    @PostMapping("/team/assignLeader")
    public String assignLeader(@RequestParam Long teamId, @RequestParam Long userId) {

        User user = this.userService.getById(userId);

        this.teamService.assignLeader(teamId, user.getUsername());

        return "redirect:/team/" + teamId;
    }

    @GetMapping("/teams")
    public ModelAndView teamsView(Model model) {

        List<Team> teams = this.teamService.getAllTeams();

        model.addAttribute("teams", teams);

        ModelAndView view = new ModelAndView();
        view.setViewName("teams");

        return view;
    }

    @PostMapping("/teams/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addTeam(@RequestParam String teamName) {

        this.teamService.createTeam(teamName);

        return "redirect:/teams";
    }

    @PostMapping("/team/join")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER', 'DEVELOPER')")
    public String joinTeam(@RequestParam Long teamId) throws IllegalAccessException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Long userId = this.userService.getByUsername(username).getId();

        this.userService.joinTeam(userId, teamId);
        return "redirect:/team/" + teamId;
    }

    @PostMapping("/team/approveRequest")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public String approveJoinRequest(@RequestParam Long teamId, @RequestParam String username) throws IllegalAccessException {

        this.teamService.approveJoinRequest(teamId, username);

        return "redirect:/team/" + teamId;
    }

    @PostMapping("/team/rejectRequest")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public String rejectJoinRequest(@RequestParam Long teamId, @RequestParam String username) throws IllegalAccessException {

        this.teamService.rejectJoinRequest(teamId, username);

        return "redirect:/team/" + teamId;
    }

    @PostMapping("/team/removeMember")
    @PreAuthorize("hasAnyRole('ADMIN', 'LEADER')")
    public String removeMember(@RequestParam Long teamId, @RequestParam Long memberId) throws IllegalAccessException {

        this.teamService.removeMember(teamId, memberId);

        return "redirect:/team/" + teamId;
    }

    @GetMapping("/team/teamProjects")
    public String teamProjectsView(@RequestParam Long teamId, Model model) {
        List<User> teamMembers = this.teamService.getTeamMembers(teamId);
        model.addAttribute("teamMembers", teamMembers);

//        List<Project> teamProjects = this.teamService.getTeamProjects(teamId);
//        model.addAttribute("teamProjects", teamProjects);

        return "team_projects";
    }
}
