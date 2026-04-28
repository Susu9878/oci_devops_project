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
        return kpiService.getTeamKpis(sprintId, teamId);
    }
 
    // USER KPIS
    @GetMapping("/users")
    public List<UserKpiDTO> getUserKpis(@RequestParam int sprintId, @RequestParam int teamId){
        return kpiService.getUserKpis(sprintId, teamId);
    }
}
