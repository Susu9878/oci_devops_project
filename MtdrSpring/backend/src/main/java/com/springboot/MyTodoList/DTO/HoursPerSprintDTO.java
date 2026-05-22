package com.springboot.MyTodoList.DTO;

public class HoursPerSprintDTO {

    private Integer sprintId;
    private String username;
    private Double totalHours;

    public Integer getSprintId() { return sprintId; }
    public void setSprintId(Integer sprintId) { this.sprintId = sprintId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Double getTotalHours() { return totalHours; }
    public void setTotalHours(Double totalHours) { this.totalHours = totalHours; }
}
