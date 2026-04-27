package com.springboot.MyTodoList.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "PROJECT")
public class Project {
    //TODO everything should be not nullable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    int projectId;
    @Column(name = "PROJECT_NAME")
    String projectName;
    @Column(name = "DESCRIPTION")
    String description;

    @OneToMany(mappedBy = "project")
    private List<Sprint> sprints;
    @OneToMany(mappedBy = "project")
    private List<Team> teams;

    public Project(){}

    public Project(int projectId, String projectName, String description){
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
    }

    public int getProjectId(){
        return projectId;
    }

    public void setProjectId(int projectId){
        this.projectId = projectId;
    }

    public String getProjectName(){
        return projectName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }
    
}