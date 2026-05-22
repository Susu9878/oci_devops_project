package com.springboot.MyTodoList.DTO;

import java.time.OffsetDateTime;

public class WorkLogDTO {
    private Double workedHours;
    private OffsetDateTime workedDay;
    // fk
    private Integer taskId;

    public double getWorkedHours() {
        return workedHours;
    }

    public OffsetDateTime getWorkedDay() {
        return workedDay;
    }

    public Integer getTaskId() {
        return taskId;
    }
}
