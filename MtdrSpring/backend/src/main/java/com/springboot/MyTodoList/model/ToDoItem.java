package com.springboot.MyTodoList.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */

@Entity
@Table(name = "TODOITEM")
public class ToDoItem {
    public enum TaskStatus {
        NOT_STARTED,
        IN_PROGRESS,
        DONE,
        NOT_DONE
    }

    public enum TaskPriority {
        LOWEST,
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @JsonProperty("taskId")
    private int taskId;
    @Column(name = "TASK_NAME") // NOT NULL
    private String taskName;
    @Column(name = "DESCRIPTION") // NOT NULL
    private String description;
    @Column(name = "STORY_POINTS")
    private Integer storyPoints;
    @Column(name = "EXPECTED_HOURS")
    private Double expectedHours;
    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITY")
    private TaskPriority priority = TaskPriority.MEDIUM;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TaskStatus status = TaskStatus.NOT_STARTED;
    @Column(name = "CREATED_AT") // NOT NULL
    private OffsetDateTime createdAt;
    @Column(name = "START_DATE")
    private OffsetDateTime startDate;
    @Column(name = "COMPLETION_DATE")
    private OffsetDateTime completionDate;
    // FK
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    // @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name = "SPRINT_ID")
    // @JsonIgnore
    private Sprint sprint;

    // TODO delete later
    @Deprecated
    @Column(name = "done")
    private boolean done;

    public ToDoItem() {

    }

    // TODO delete later
    @Transient
    @JsonProperty("done")
    public boolean isDone() {
        return done;
    }

    public ToDoItem(Integer taskId, String taskName, String description,
            Integer storyPoints, Double expectedHours, TaskPriority priority,
            TaskStatus status, OffsetDateTime createdAt, OffsetDateTime startDate,
            OffsetDateTime completionDate, User userId, Sprint sprintId) {

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
        this.user = userId;
        this.sprint = sprintId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(Integer storyPoints) {
        this.storyPoints = storyPoints;
    }

    public Double getExpectedHours() {
        return expectedHours;
    }

    public void setExpectedHours(Double expectedHours) {
        this.expectedHours = expectedHours;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(OffsetDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
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