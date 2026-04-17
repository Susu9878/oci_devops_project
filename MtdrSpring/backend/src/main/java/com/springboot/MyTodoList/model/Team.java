package com.springboot.MyTodoList.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TEAM")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int teamId;

    @Column(name = "TEAM_NAME")
    String teamName;

    @Column(name = "PROJECT_ID")
    private Integer projectId;

    public Team(){}

    public Team(int teamId, String teamName, Integer projectId){
        this.teamId = teamId;
        this.teamName = teamName;
        this.projectId = projectId;
    }

    public int getTeamId(){
        return teamId;
    }

    public void setTeamId(int teamId){
        this.teamId = teamId;
    }

    public String getTeamName(){
        return teamName;
    }

    public void setTeamName(String teamName){
        this.teamName = teamName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    
}
