package com.springboot.MyTodoList.DTO;

public class TeamSprintKpiDTO {

    private Double avgTasksPerUser;
    private Double avgHoursPerUser;
    private Long totalTasksCompleted;
    private Long totalTasksAssigned;
    private Double totalHoursWorked;

    // Getters & Setters
    public Double getAvgTasksPerUser() { return avgTasksPerUser; }
    public void setAvgTasksPerUser(Double avgTasksPerUser) { this.avgTasksPerUser = avgTasksPerUser; }

    public Double getAvgHoursPerUser() { return avgHoursPerUser; }
    public void setAvgHoursPerUser(Double avgHoursPerUser) { this.avgHoursPerUser = avgHoursPerUser; }

    public Long getTotalTasksCompleted() { return totalTasksCompleted; }
    public void setTotalTasksCompleted(Long totalTasksCompleted) { this.totalTasksCompleted = totalTasksCompleted; }

    public Long getTotalTasksAssigned() { return totalTasksAssigned; }
    public void setTotalTasksAssigned(Long totalTasksAssigned) { this.totalTasksAssigned = totalTasksAssigned; }

    public Double getTotalHoursWorked() { return totalHoursWorked; }
    public void setTotalHoursWorked(Double totalHoursWorked) { this.totalHoursWorked = totalHoursWorked; }
}