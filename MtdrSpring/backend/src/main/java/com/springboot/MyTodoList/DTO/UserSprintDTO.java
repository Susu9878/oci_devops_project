package com.springboot.MyTodoList.DTO;

public class UserSprintDTO {
    private Integer userId;
    private String username;

    private Double avgTasksPerSprint;
    private Double avgHoursPerSprint;

    private Long totalTasksCompleted;
    private Long totalTasksAssigned;
    private Double totalHoursWorked;

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setAvgTasksPerSprint(Double avgTasksPerSprint) {
        this.avgTasksPerSprint = avgTasksPerSprint;
    }

    public Double getAvgTasksPerSprint() {
        return avgTasksPerSprint;
    }

    public void setAvgHoursPerSprint(Double avgHoursPerSprint) {
        this.avgHoursPerSprint = avgHoursPerSprint;
    }

    public Double getAvgHoursPerSprint() {
        return avgHoursPerSprint;
    }

    public Long getTotalTasksCompleted() {
        return totalTasksCompleted;
    }

    public void setTotalTasksCompleted(Long totalTasksCompleted) {
        this.totalTasksCompleted = totalTasksCompleted;
    }

    public Long getTotalTasksAssigned() {
        return totalTasksAssigned;
    }

    public void setTotalTasksAssigned(Long totalTasksAssigned) {
        this.totalTasksAssigned = totalTasksAssigned;
    }

    public Double getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(Double totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }
}
