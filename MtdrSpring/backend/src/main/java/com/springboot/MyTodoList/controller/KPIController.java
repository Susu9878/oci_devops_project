package com.springboot.MyTodoList.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

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

    // TEAM KPIs
    @GetMapping("/team")
    public TeamSprintKpiDTO getTeamKpis(@RequestParam int sprintId, @RequestParam int teamId) {
        System.out.println("➡️ [KPIController] /team called");
        System.out.println("   sprintId=" + sprintId + ", teamId=" + teamId);

        TeamSprintKpiDTO result = kpiService.getTeamKpis(sprintId, teamId);

        System.out.println("⬅️ [KPIController] returning: " + result);

        return result;
    }
 
    // USER KPIS
    @GetMapping("/users")
    public List<UserKpiDTO> getUserKpis(@RequestParam int sprintId, @RequestParam int teamId){
        System.out.println("➡️ [KPIController] /users called");
        System.out.println("   sprintId=" + sprintId + ", teamId=" + teamId);

        List<UserKpiDTO> result = kpiService.getUserKpis(sprintId, teamId);

        System.out.println("⬅️ [KPIController] users count: " + result.size());

        return result;
    }
}
