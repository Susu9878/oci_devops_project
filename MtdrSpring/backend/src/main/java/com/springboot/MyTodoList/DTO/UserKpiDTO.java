package com.springboot.MyTodoList.DTO;

public class UserKpiDTO {

    private Integer userId;
    private String username;
    private Long tasksCompleted;
    private Long tasksInProgress;
    private Long tasksNotStarted;
    private Long tasksNotDone;
    private Double hoursWorked;

    // Getters & Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getTasksCompleted() { return tasksCompleted; }
    public void setTasksCompleted(Long tasksCompleted) { this.tasksCompleted = tasksCompleted; }

    public Long getTasksInProgress() { return tasksInProgress; }
    public void setTasksInProgress(Long tasksInProgress) { this.tasksInProgress = tasksInProgress; }

    public Long getTasksNotStarted() { return tasksNotStarted; }
    public void setTasksNotStarted(Long tasksNotStarted) { this.tasksNotStarted = tasksNotStarted; }

    public Long getTasksNotDone() { return tasksNotDone; }
    public void setTasksNotDone(Long tasksNotDone) { this.tasksNotDone = tasksNotDone; }

    public Double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Double hoursWorked) { this.hoursWorked = hoursWorked; }
}