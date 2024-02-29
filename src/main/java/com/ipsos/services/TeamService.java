package com.ipsos.services;

import com.ipsos.entities.Team;

import java.util.List;

public interface TeamService {
    Team createTeam(String name);
    List<Team> getAllTeams();
    Team getById(Long teamId);

    void assignLeader(Long teamId, String leaderUsername);
    void approveJoinRequest(Long teamId, String username);
    void rejectJoinRequest(Long teamId, String username);
    void removeMember(Long teamId, Long memberId);
}
