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
        System.out.println("➡️ getTeamKpis START");
        List<Object[]> results = workLogRepository.getTeamKpisRaw(sprintId, teamId);

        if (results.isEmpty()) {
            System.out.println("⚠️ No data returned");
            return new TeamSprintKpiDTO();
        }

        Object[] row = results.get(0);
        System.out.println("📊 Row length: " + row.length);
        for (int i = 0; i < row.length; i++) {
            System.out.println("col[" + i + "] = " + row[i]);
        }

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
        System.out.println("✅ DTO created");

        return dto;
    }

    // general user kpis
    public List<UserKpiDTO> getUserKpis(int sprintId, int teamId) {
        System.out.println("➡️ [KPIService] getUserKpis START");
        List<Object[]> results = toDoItemRepository.getUserKpisRaw(sprintId, teamId);

        System.out.println("📊 Rows returned: " + results.size());

        return results.stream().map(row -> {

            System.out.println("---- ROW ----");
            for (int i = 0; i < row.length; i++) {
                System.out.println("col[" + i + "] = " + row[i]);
            }

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
}
