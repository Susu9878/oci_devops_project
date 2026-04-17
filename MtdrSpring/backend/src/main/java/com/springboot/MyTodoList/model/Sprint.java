package com.springboot.MyTodoList.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "SPRINT")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int sprintId;

    @Column(name = "START_DATE")
    private OffsetDateTime startDate;
    
    @Column(name = "END_DATE")
    private OffsetDateTime endDate;

    @Column(name = "STATUS")
    String status;

    @Column(name = "PROJECT_ID")
    private Integer projectId;

    public Sprint(){}

    public Sprint(int sprintId, OffsetDateTime startDate, OffsetDateTime endDate, String status, Integer projectId){
        this.sprintId = sprintId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.projectId = projectId;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    
}
