package com.springboot.MyTodoList.model;


import jakarta.persistence.*;

import java.time.OffsetDateTime;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "TASKITEM")
public class TaskItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    int taskId;
    
    @Column(name = "task_name", nullable = false)
    private String taskName;
    @Column(name = "description")
    private String description;
    @Column(name = "story_points")
    private Integer storyPoints;
    @Column(name = "expected_hours")
    private Double expectedHours;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "start_date")
    private OffsetDateTime startDate;
    @Column(name = "completion_date")
    private OffsetDateTime completionDate;
    @Column(name = "user_id")
    private Integer userId; 
    @Column(name = "sprint_id")
    private Integer sprintId; 
    @Column(name = "done")
    private boolean done;
    public TaskItem(){

    }
    public TaskItem(Integer taskId, String taskName, String description,
                Integer storyPoints, Double expectedHours, Integer priority,
                String status, OffsetDateTime createdAt, OffsetDateTime startDate,
                OffsetDateTime completionDate, Integer userId, Integer sprintId) {

        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.storyPoints = storyPoints;
        this.expectedHours = expectedHours;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.startDate = startDate;
        this.completionDate = completionDate;
        this.userId = userId;
        this.sprintId = sprintId;
    }

    public int getID() {
        return taskId;
    }

    public void setID(int ID) {
        this.taskId = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreation_ts() {
        return createdAt;
    }

    public void setCreation_ts(OffsetDateTime created_at) {
        this.createdAt = created_at;
    }

    //left is done for current functionallity of frontend and services. TODO: delete later
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", status='" + status + '\'' +
                ", priority=" + priority +
                '}';
    }
}
