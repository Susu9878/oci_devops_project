package com.springboot.MyTodoList.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PROJECT")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int projectId;

    @Column(name = "PROJECT_NAME")
    String projectName;

    @Column(name = "DESCRIPTION")
    String description;

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
