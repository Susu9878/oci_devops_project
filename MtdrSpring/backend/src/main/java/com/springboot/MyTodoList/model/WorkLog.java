package com.springboot.MyTodoList.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "WORK_LOG")
public class WorkLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int workLogId;

    @Column(name = "WORKED_HOURS")
    Double workedHours;

    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt;
    
    @Column(name = "WORKED_DAY")
    private OffsetDateTime workedDay;

    @Column(name = "TASK_ID")
    private Integer taskId;

    @Column(name = "USER_ID")
    private Integer userId;

    public WorkLog(){}

    public WorkLog(int workLogId, Double workedHours, OffsetDateTime createdAt, OffsetDateTime workedDay, Integer taskId, Integer userId){
        this.workLogId = workLogId;
        this.workedHours = workedHours;
        this.createdAt = createdAt;
        this.workedDay = workedDay;
        this.taskId = taskId;
        this.userId = userId;
    }

    public int getWorkLogId(){
        return workLogId;
    }

    public void setWorkLogId(int workLogId){
        this.workLogId = workLogId;
    }

    public Double getWorkedHours(){
        return workedHours;
    }

    public void setWorkedHours(Double workedHours){
        this.workedHours = workedHours;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getWorkedDay() {
        return workedDay;
    }

    public void setWorkedDay(OffsetDateTime workedDay) {
        this.workedDay = workedDay;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
}
