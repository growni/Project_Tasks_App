package com.ipsos.services;

import com.ipsos.entities.Project;
import com.ipsos.entities.Team;
import com.ipsos.entities.User;

import java.util.List;

public interface TeamService {
    Team createTeam(String name);
    List<Team> getAllTeams();
    Team getById(Long teamId);

    void assignLeader(Long teamId, String leaderUsername);
    void approveJoinRequest(Long teamId, String username) throws IllegalAccessException;
    void rejectJoinRequest(Long teamId, String username) throws IllegalAccessException;
    void removeMember(Long teamId, Long memberId) throws IllegalAccessException;
    List<User> getTeamMembers(Long teamId);
    List<Project> getTeamProjects(Long teamId);
}
