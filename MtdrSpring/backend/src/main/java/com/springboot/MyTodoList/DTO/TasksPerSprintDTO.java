package com.springboot.MyTodoList.DTO;

public class TasksPerSprintDTO {

    private Integer sprintId;
    private String username;
    private Long completedTasks;

    public Integer getSprintId() { return sprintId; }
    public void setSprintId(Integer sprintId) { this.sprintId = sprintId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(Long completedTasks) { this.completedTasks = completedTasks; }
}
