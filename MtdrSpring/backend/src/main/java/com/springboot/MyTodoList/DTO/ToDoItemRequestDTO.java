package com.springboot.MyTodoList.DTO;

import com.springboot.MyTodoList.model.ToDoItem;

public class ToDoItemRequestDTO {
    private String taskName;
    private String description;
    private Integer storyPoints;
    private Double expectedHours;
    private String priority;
    private String status;
    private Integer userId;
    private Integer sprintId;

    public String getTaskName()             { return taskName; }
    public void setTaskName(String v)       { taskName = v; }
    public String getDescription()          { return description; }
    public void setDescription(String v)    { description = v; }
    public Integer getStoryPoints()         { return storyPoints; }
    public void setStoryPoints(Integer v)   { storyPoints = v; }
    public Double getExpectedHours()        { return expectedHours; }
    public void setExpectedHours(Double v)  { expectedHours = v; }
    public String getPriority()             { return priority; }
    public void setPriority(String v)       { priority = v; }
    public String getStatus()               { return status; }
    public void setStatus(String v)         { status = v; }
    public Integer getUserId()              { return userId; }
    public void setUserId(Integer v)        { userId = v; }
    public Integer getSprintId()            { return sprintId; }
    public void setSprintId(Integer v)      { sprintId = v; }

    public ToDoItem toEntity() {
        ToDoItem item = new ToDoItem();
        item.setTaskName(taskName);
        item.setDescription(description);
        item.setStoryPoints(storyPoints);
        item.setExpectedHours(expectedHours);
        if (priority != null) item.setPriority(ToDoItem.TaskPriority.valueOf(priority));
        if (status != null)   item.setStatus(ToDoItem.TaskStatus.valueOf(status));
        return item;
    }
}