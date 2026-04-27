package com.springboot.MyTodoList.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "TEAM")
public class Team {
    //TODO change everything to not nullable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    int teamId;
    @Column(name = "TEAM_NAME")
    String teamName;
    //fk
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @OneToMany(mappedBy = "team")
    private List<User> users;

    public Team(){}

    public Team(int teamId, String teamName, Project projectId){
        this.teamId = teamId;
        this.teamName = teamName;
        this.project = projectId;
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

    public Project getProjectId() {
        return project;
    }

    public void setProjectId(Project projectId) {
        this.project = projectId;
    }
    
}