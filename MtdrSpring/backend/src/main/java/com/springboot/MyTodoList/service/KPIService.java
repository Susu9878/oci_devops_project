package com.springboot.MyTodoList.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.DTO.HoursPerSprintDTO;
import com.springboot.MyTodoList.DTO.TasksPerSprintDTO;
import com.springboot.MyTodoList.DTO.TeamSprintKpiDTO;
import com.springboot.MyTodoList.DTO.UserKpiDTO;
import com.springboot.MyTodoList.repository.UserRepository;
import com.springboot.MyTodoList.repository.WorkLogRepository;

@Service
public class KPIService {
    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private UserRepository userRepository;

    // General team kpis PER sprint
    public TeamSprintKpiDTO getTeamKpiSprint(int sprintId, int teamId) {
        List<Object[]> results = workLogRepository.getTeamKpisPerSprint(sprintId, teamId);
        if (results.isEmpty()) {
            return new TeamSprintKpiDTO();
        }

        Object[] row = results.get(0);
        long totalTasks = row[0] != null ? ((Number) row[0]).longValue() : 0;
        long completedTasks = row[1] != null ? ((Number) row[1]).longValue() : 0;
        long totalUsers = row[2] != null ? ((Number) row[2]).longValue() : 0;
        double totalHours = row[3] != null ? ((Number) row[3]).doubleValue() : 0;

        TeamSprintKpiDTO dto = new TeamSprintKpiDTO();

        dto.setTotalTasksAssigned(totalTasks);
        dto.setTotalTasksCompleted(completedTasks);
        dto.setTotalHoursWorked(totalHours);
        dto.setAvgTasksPerUser(totalUsers == 0 ? 0 : (double) totalTasks / totalUsers);
        dto.setAvgHoursPerUser(totalUsers == 0 ? 0 : totalHours / totalUsers);

        return dto;
    }

    // general user kpis PER sprint
    public List<UserKpiDTO> getUserKpiSprint(int sprintId, int teamId) {
        List<Object[]> results = userRepository.getUserKpisPerSprint(sprintId, teamId);

        return results.stream().map(row -> {
            UserKpiDTO dto = new UserKpiDTO();

            dto.setUserId(((Number) row[0]).intValue());
            dto.setUsername((String) row[1]);
            dto.setTasksCompleted(((Number) row[2]).longValue());
            dto.setTasksInProgress(((Number) row[3]).longValue());
            dto.setTasksNotStarted(((Number) row[4]).longValue());
            dto.setTasksNotDone(row[5] != null ? ((Number) row[5]).longValue() : 0);
            dto.setHoursWorked(((Number) row[6]).doubleValue());

            return dto;
        }).toList();
    }

    //team kpis for all sprints
    public TeamSprintKpiDTO getTeamKpisForTeamOnly(int teamId) {
        List<Object[]> results = workLogRepository.getTeamKpisAllSprints(teamId);

        if (results.isEmpty()) {
            return new TeamSprintKpiDTO();
        }

        Object[] row = results.get(0);

        long totalTasks = row[0] != null ? ((Number) row[0]).longValue() : 0;
        long completedTasks = row[1] != null ? ((Number) row[1]).longValue() : 0;
        long totalUsers = row[2] != null ? ((Number) row[2]).longValue() : 0;
        double totalHours = row[3] != null ? ((Number) row[3]).doubleValue() : 0;

        TeamSprintKpiDTO dto = new TeamSprintKpiDTO();

        dto.setTotalTasksAssigned(totalTasks);
        dto.setTotalTasksCompleted(completedTasks);
        dto.setTotalHoursWorked(totalHours);
        dto.setAvgTasksPerUser(totalUsers == 0 ? 0 : (double) totalTasks / totalUsers);
        dto.setAvgHoursPerUser(totalUsers == 0 ? 0 : totalHours / totalUsers);

        return dto;
    }

    //Graph 1 service
    public List<TasksPerSprintDTO> getTasksPerSprint(int teamId) {
        List<Object[]> results = workLogRepository.getCompletedTasksPerUserPerSprint(teamId);

        return results.stream().map(row -> {
            TasksPerSprintDTO dto = new TasksPerSprintDTO();

            // row[1] = userId 
            dto.setSprintId(((Number) row[0]).intValue());
            dto.setUsername((String) row[2]);
            dto.setCompletedTasks(((Number) row[3]).longValue()); 

            return dto;
        }).toList();
    }

    //Graph 1 service
    public List<HoursPerSprintDTO> getHoursPerSprint(int teamId) {
        List<Object[]> results = workLogRepository.getHoursPerUserPerSprint(teamId);

        return results.stream().map(row -> {
            HoursPerSprintDTO dto = new HoursPerSprintDTO();

            dto.setSprintId(row[0] != null ? ((Number) row[0]).intValue() : 0);
            dto.setTotalHours(row[1] != null ? ((Number) row[1]).doubleValue() : 0);
            dto.setUsername((String) row[2]);

            return dto;
        }).toList();
    }
}
