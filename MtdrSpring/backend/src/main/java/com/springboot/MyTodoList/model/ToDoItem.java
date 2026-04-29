package com.springboot.MyTodoList.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @Column(name = "TASK_NAME")
    private String taskName;

    @Column(name = "DESCRIPTION")
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

    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt;

    @Column(name = "START_DATE")
    private OffsetDateTime startDate;

    @Column(name = "COMPLETION_DATE")
    private OffsetDateTime completionDate;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "SPRINT_ID")
    @JsonIgnore
    private Sprint sprint;

    //TODO delete later
    @Column(name = "done")
    private boolean done;

    public ToDoItem() {}

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

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getTaskId() { return taskId; }
    public void setTaskId(int ID) { this.taskId = ID; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStoryPoints() { return storyPoints; }
    public void setStoryPoints(Integer storyPoints) { this.storyPoints = storyPoints; }

    public Double getExpectedHours() { return expectedHours; }
    public void setExpectedHours(Double expectedHours) { this.expectedHours = expectedHours; }

    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public OffsetDateTime getCreation_ts() { return createdAt; }
    public void setCreation_ts(OffsetDateTime created_at) { this.createdAt = created_at; }

    public OffsetDateTime getStartDate() { return startDate; }
    public void setStartDate(OffsetDateTime startDate) { this.startDate = startDate; }

    public OffsetDateTime getCompletionDate() { return completionDate; }
    public void setCompletionDate(OffsetDateTime completionDate) { this.completionDate = completionDate; }

    //TODO delete later
    @Transient
    @JsonProperty("done")
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

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