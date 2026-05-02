package com.springboot.MyTodoList.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.DTO.HoursPerSprintDTO;
import com.springboot.MyTodoList.DTO.TasksPerSprintDTO;
import com.springboot.MyTodoList.DTO.TeamSprintKpiDTO;
import com.springboot.MyTodoList.DTO.UserKpiDTO;
import com.springboot.MyTodoList.service.KPIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/todolist/kpis")
@CrossOrigin
public class KPIController {
    @Autowired
    private KPIService kpiService;

    /* GETS team statisctics PER sprint
        {
            avgTasksPerUser: num,
            avgHoursPerUser: num,
            totalTasksCompleted: num,
            totalTasksAssigned: num,
            totalHoursWorked: num,
        }
    */
    @GetMapping("/team/sprint")
    public TeamSprintKpiDTO getTeamKpiSprint(@RequestParam int sprintId, @RequestParam int teamId) {
        TeamSprintKpiDTO result = kpiService.getTeamKpiSprint(sprintId, teamId);
        return result;
    }
 
    /* GETS a list of each user in a team PER sprint:
        {
            userId: num,
            username: String,
            tasksCompleded: num,
            tasksInProgress: num,
            tasksNotStarted: num,
            tasksNotDone: num,
            hoursWorked: num,
        },
        {
            second user
        }
    */ 
    @GetMapping("/users/sprint")
    public List<UserKpiDTO> getUserKpiSprint(@RequestParam int sprintId, @RequestParam int teamId){
        List<UserKpiDTO> result = kpiService.getUserKpiSprint(sprintId, teamId);
        return result;
    }

    /* same return as getTeamKpiSprint but for all sprints
    {
        "avgTasksPerUser": num,
        "avgHoursPerUser": num,
        "totalTasksCompleted": num,
        "totalTasksAssigned": num,
        "totalHoursWorked": num
    } */
    @GetMapping("/team")
    public TeamSprintKpiDTO getTeamKpis(@RequestParam int teamId) {
        return kpiService.getTeamKpisForTeamOnly(teamId); 
    }

    //graph #1 endpoint
    @GetMapping("/tasks-per-sprint")
    public List<TasksPerSprintDTO> getTasksPerSprint(@RequestParam int teamId) {
        return kpiService.getTasksPerSprint(teamId);
    }

    //graph #2 endpoint
    @GetMapping("/hours-per-sprint")
    public List<HoursPerSprintDTO> getHoursPerSprint(@RequestParam int teamId) {
        return kpiService.getHoursPerSprint(teamId);
    }
}
