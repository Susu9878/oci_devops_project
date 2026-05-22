package com.springboot.MyTodoList.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.DTO.WorkLogDTO;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.model.WorkLog;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.springboot.MyTodoList.repository.UserRepository;
import com.springboot.MyTodoList.repository.WorkLogRepository;

@Service
public class WorkLogService {
    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private ToDoItemRepository toDoItemRepository;
    @Autowired
    private UserRepository userRepository;

    public void logHours(WorkLogDTO request) {
        ToDoItem task = toDoItemRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        WorkLog workLog = new WorkLog();

        workLog.setWorkedHours(request.getWorkedHours());
        workLog.setWorkedDay(request.getWorkedDay());
        workLog.setCreatedAt(OffsetDateTime.now());

        // Fk
        workLog.setTaskId(task);
        workLog.setUserId(task.getUserId());

        workLogRepository.save(workLog);
    }
}
