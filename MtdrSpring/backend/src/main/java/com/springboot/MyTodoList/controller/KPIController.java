package com.springboot.MyTodoList.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DTO.TeamSprintKpiDTO;
import com.DTO.UserKpiDTO;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.WorkLog;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.springboot.MyTodoList.repository.UserRepository;
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
public class KPIController {
    //Model Repositories
    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private ToDoItemRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    // 1-4 team KPIs
    public TeamSprintKpiDTO getTeamSprintKpis(int sprintId, int teamId){
        List<User> users = userRepository.findByTeamId(teamId);

        List<ToDoItem> tasks = taskRepository.findBySprintSprintIdAndUserTeamTeamId(sprintId, teamId);

        List<WorkLog> logs = workLogRepository.findByTaskSprintSprintIdAndUserTeamTeamId(sprintId, teamId);

        int userCount = users.size();
        long totalTasks = tasks.size();
        long completedTasks = tasks.stream()
                .filter(t -> "DONE".equalsIgnoreCase(t.getStatus()))
                .count();
        double totalHours = logs.stream()
                .mapToDouble(WorkLog::getWorkedHours)
                .sum();

        TeamSprintKpiDTO dto = new TeamSprintKpiDTO();
        dto.setAvgTasksPerUser(userCount == 0 ? 0 : (double) totalTasks / userCount);
        dto.setAvgHoursPerUser(userCount == 0 ? 0 : totalHours / userCount);
        dto.setTotalTasksAssigned(totalTasks);
        dto.setTotalTasksCompleted(completedTasks);
        dto.setTotalHoursWorked(totalHours);

        return dto;
    };

    // 5 & 6 user-level KPIs
    public List<UserKpiDTO> getUserKpis(int sprintId, int teamId) {

        List<User> users = userRepository.findByTeamTeamId(teamId);

        return users.stream().map(user -> {

            List<ToDoItem> tasks = taskRepository
                    .findBySprintSprintIdAndUserUserId(sprintId, user.getUserId());

            List<WorkLog> logs = workLogRepository
                    .findByUserUserIdAndTaskSprintSprintId(user.getUserId(), sprintId);

            long completed = tasks.stream()
                    .filter(t -> "DONE".equalsIgnoreCase(t.getStatus()))
                    .count();

            long inProgress = tasks.stream()
                    .filter(t -> "IN_PROGRESS".equalsIgnoreCase(t.getStatus()))
                    .count();

            long notStarted = tasks.stream()
                    .filter(t -> "NOT_STARTED".equalsIgnoreCase(t.getStatus()))
                    .count();

            double hours = logs.stream()
                    .mapToDouble(WorkLog::getWorkedHours)
                    .sum();

            UserKpiDTO dto = new UserKpiDTO();
            dto.setUserId(user.getUserId());
            dto.setUsername(user.getUsername());
            dto.setTasksCompleted(completed);
            dto.setTasksInProgress(inProgress);
            dto.setTasksNotStarted(notStarted);
            dto.setHoursWorked(hours);

            return dto;
        }).toList();
    }
}
