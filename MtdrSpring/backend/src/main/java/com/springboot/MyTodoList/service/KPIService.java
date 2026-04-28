package com.springboot.MyTodoList.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.DTO.TeamSprintKpiDTO;
import com.springboot.MyTodoList.DTO.UserKpiDTO;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.springboot.MyTodoList.repository.WorkLogRepository;

/*
KPIs 
    1. one that returns the average of tasks assigned per user this sprint id by specific team id 
    2. one that returns the average hours worked per user this sprint by specific team id 
    3. one that returns the total tasks completed and total tasks assigned this sprint id by team id 
    4. total hours worked by users this sprint id by team id 
    5. one that returns an object with the status of tasks(not done, in progress, completed) by 
        specific users this sprint id by team id 
    6. one that returns the hours worked by specific users this sprint id by team id
*/
@Service
public class KPIService {
    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private ToDoItemRepository toDoItemRepository;

    // General team kpis
    public TeamSprintKpiDTO getTeamKpis(int sprintId, int teamId) {
        Object[] result = workLogRepository.getTeamKpisRaw(sprintId, teamId);

        long totalTasks = ((Number) result[0]).longValue();
        long completedTasks = ((Number) result[1]).longValue();
        long totalUsers = ((Number) result[2]).longValue();
        double totalHours = ((Number) result[3]).doubleValue();

        TeamSprintKpiDTO dto = new TeamSprintKpiDTO();

        dto.setTotalTasksAssigned(totalTasks);
        dto.setTotalTasksCompleted(completedTasks);
        dto.setTotalHoursWorked(totalHours);

        dto.setAvgTasksPerUser(totalUsers == 0 ? 0 : (double) totalTasks / totalUsers);
        dto.setAvgHoursPerUser(totalUsers == 0 ? 0 : totalHours / totalUsers);

        return dto;
    }

    //general user kpis
    public List<UserKpiDTO> getUserKpis(int sprintId, int teamId) {
        List<Object[]> results = toDoItemRepository.getUserKpisRaw(sprintId, teamId);

        return results.stream().map(row -> {
            UserKpiDTO dto = new UserKpiDTO();

            dto.setUserId(((Number) row[0]).intValue());
            dto.setUsername((String) row[1]);
            dto.setTasksCompleted(((Number) row[2]).longValue());
            dto.setTasksInProgress(((Number) row[3]).longValue());
            dto.setTasksNotStarted(((Number) row[4]).longValue());
            dto.setHoursWorked(((Number) row[5]).doubleValue());

            return dto;
        }).toList();
    }
}
