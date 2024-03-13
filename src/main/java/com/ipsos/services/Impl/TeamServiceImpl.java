package com.ipsos.services.Impl;

import com.ipsos.entities.Project;
import com.ipsos.entities.Team;
import com.ipsos.entities.User;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.exceptions.InvalidDataException;
import com.ipsos.exceptions.UserAlreadyAssignedException;
import com.ipsos.repositories.TeamRepository;
import com.ipsos.repositories.UserRepository;
import com.ipsos.services.TeamService;
import com.ipsos.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ipsos.constants.ErrorMessages.TeamOperations.*;
import static com.ipsos.constants.ErrorMessages.UserOperations.USER_ALREADY_HAS_TEAM;
import static com.ipsos.constants.Regex.NAME_REGEX;

@Service
public class TeamServiceImpl implements TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserService userService;

    public TeamServiceImpl(UserRepository userRepository, TeamRepository teamRepository, UserService userService) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.userService = userService;
    }

    @Override
    public Team createTeam(String name) {
        Team team = new Team();

        if(!name.matches(NAME_REGEX)) {
            throw new InvalidDataException(String.format(INVALID_TEAM_NAME, name));
        }

        team.setName(name);
        this.teamRepository.save(team);

        return team;
    }

    @Override
    public void assignLeader(Long teamId, String leaderUsername) {
        Team team = getById(teamId);

        User user = this.userService.getByUsername(leaderUsername);

        if(user.getTeam() != null) {
            if(team.getTeamLeader() != null && team.getTeamLeader().getUsername().equals(leaderUsername)) {
                throw new InvalidDataException(String.format(USER_ALREADY_LEADER, leaderUsername, team.getName()));
            }
        }

        User currentTeamLeader = team.getTeamLeader();

        clearTeamLeaderRoles(team.getId());

        if(currentTeamLeader != null && !currentTeamLeader.equals(user)) {
//            this.userService.removeRole(currentTeamLeader.getId(), "ROLE_LEADER");
            this.userRepository.save(currentTeamLeader);
        }

        user.setTeam(team);
        this.userService.addRole(user.getId(), "ROLE_LEADER");
        this.userRepository.save(user);

        team.setTeamLeader(user);
        this.teamRepository.save(team);
    }

    private void clearTeamLeaderRoles(Long teamId) {
        Team team = getById(teamId);

        for (User member : team.getMembers()) {
            if (this.userService.hasRole(member.getId(), "ROLE_LEADER")) {
                this.userService.removeRole(member.getId(), "ROLE_LEADER");
            }
        }
    }

    @Override
    public Team getById(Long teamId) {
        return this.teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityMissingFromDatabase(TEAM_NOT_FOUND));
    }

    @Override
    public List<Team> getAllTeams() {
        return this.teamRepository.findAll();
    }

    @Override
    public void approveJoinRequest(Long teamId, String username) throws IllegalAccessException {
        User user = this.userService.getByUsername(username);

        if(user.getTeam() != null) {
            throw new UserAlreadyAssignedException(String.format(USER_ALREADY_HAS_TEAM, username, user.getTeam().getName()));
        }

        validateUserAction(teamId);

        Team team = getById(teamId);

        user.setTeam(team);
        this.userRepository.save(user);

        team.getMembers().add(user);
        this.teamRepository.save(team);

        removeJoinRequestsFromAllTeams(username);
    }

    private void validateUserAction(Long teamId) throws IllegalAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();

        User loggedUser = this.userService.getByUsername(loggedUsername);
        Team team = getById(teamId);

        if(!this.userService.hasRole(loggedUser.getId(), "ROLE_ADMIN") &&
                (loggedUser.getTeam() == null || !loggedUser.getTeam().getId().equals(teamId))) {
            throw new IllegalAccessException(String.format(USER_NOT_LEADER_OF_THIS_TEAM, loggedUsername, team.getName()));
        }

    }

    @Override
    public void rejectJoinRequest(Long teamId, String username) throws IllegalAccessException {
        User user = this.userService.getByUsername(username);
        Team team = getById(teamId);

        validateUserAction(teamId);

        team.getJoinRequestUsernames().remove(user.getUsername());

        this.teamRepository.save(team);
    }

    private void removeJoinRequestsFromAllTeams(String username) {
        List<Team> teams = this.teamRepository.findAll();

        for (Team team : teams) {
            team.getJoinRequestUsernames().remove(username);
        }

        this.teamRepository.saveAll(teams);
    }

    @Override
    public void removeMember(Long teamId, Long memberId) throws IllegalAccessException {
        Team team = getById(teamId);
        User user = this.userService.getById(memberId);

        if(!user.getTeam().getId().equals(team.getId())) {
            throw new EntityMissingFromDatabase(String.format(USER_NOT_IN_TEAM, user.getUsername(), team.getName()));
        }

        validateUserAction(teamId);

        if(this.userService.hasRole(memberId, "ROLE_LEADER")) {
            this.userService.removeRole(memberId, "ROLE_LEADER");
            team.setTeamLeader(null);
        }

        team.getMembers().remove(user);
        this.teamRepository.save(team);

        user.setTeam(null);
        this.userRepository.save(user);
    }

    @Override
    public List<User> getTeamMembers(Long teamId) {
        Team team = getById(teamId);

        return team.getMembers();
    }

    @Override
    public List<Project> getTeamProjects(Long teamId) {
        Team team = getById(teamId);
        List<User> members = team.getMembers();

        List<Project> teamProjects = new ArrayList<>();

        for (User member : members) {
            teamProjects.addAll(member.getProjects());
        }

        return teamProjects;
    }
}
