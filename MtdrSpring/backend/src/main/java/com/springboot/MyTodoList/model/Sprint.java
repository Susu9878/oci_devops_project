package com.springboot.MyTodoList.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "SPRINT")
public class Sprint {
    public enum SprintStatus {
        NOT_ACTIVE,
        ACTIVE,
        CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPRINT_ID")
    private int sprintId;
    @Column(name = "SPRINT_NAME")
    private String sprintName;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private SprintStatus status = SprintStatus.NOT_ACTIVE;
    @Column(name = "START_DATE")//nullable
    private OffsetDateTime startDate;
    @Column(name = "END_DATE")//nullable
    private OffsetDateTime endDate;
    //FK
    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    public Sprint(){}

    public Sprint(int sprintId, OffsetDateTime startDate, OffsetDateTime endDate, SprintStatus status, Project projectId){
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

    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }

    public Project getProjectId() {
        return project;
    }

    public void setProjectId(Project projectId) {
        this.project = projectId;
    }
    
}
