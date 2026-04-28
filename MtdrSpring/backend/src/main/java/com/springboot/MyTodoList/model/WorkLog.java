package com.springboot.MyTodoList.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "WORK_LOG")
public class WorkLog {
    //TODO everything should be not nullable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_LOG_ID")
    int workLogId;
    @Column(name = "WORKED_HOURS")
    Double workedHours;
    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt;
    @Column(name = "WORKED_DAY")
    private OffsetDateTime workedDay;
    //FK
    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private ToDoItem task;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public WorkLog(){}

    public WorkLog(int workLogId, Double workedHours, OffsetDateTime createdAt, OffsetDateTime workedDay, ToDoItem taskId, User userId){
        this.workLogId = workLogId;
        this.workedHours = workedHours;
        this.createdAt = createdAt;
        this.workedDay = workedDay;
        this.task = taskId;
        this.user = userId;
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

    public ToDoItem getTaskId() {
        return task;
    }

    public void setTaskId(ToDoItem taskId) {
        this.task = taskId;
    }

    public User getUserId() {
        return user;
    }

    public void setUserId(User userId) {
        this.user = userId;
    }
    
}
