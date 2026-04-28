package com.springboot.MyTodoList.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "SPRINT")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPRINT_ID")
    int sprintId;
    @Column(name = "SPRINT_NAME")
    String sprintName;
    @Column(name = "STATUS")//TODO add constraints, not nullable
    String status;
    @Column(name = "START_DATE")//nullable
    private OffsetDateTime startDate;
    @Column(name = "END_DATE")//nullable
    private OffsetDateTime endDate;
    //FK
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    public Sprint(){}

    public Sprint(int sprintId, OffsetDateTime startDate, OffsetDateTime endDate, String status, Project projectId){
        this.sprintId = sprintId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.project = projectId;
    }

    public int getSprintId(){
        return sprintId;
    }

    public void setSprintId(int sprintId){
        this.sprintId = sprintId;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Project getProjectId() {
        return project;
    }

    public void setProjectId(Project projectId) {
        this.project = projectId;
    }
    
}
