package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.DTO.WorkLogDTO;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.WorkLog;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.springboot.MyTodoList.repository.WorkLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkLogService {

    @Autowired
    private WorkLogRepository workLogRepository;
    @Autowired
    private ToDoItemRepository toDoItemRepository;

    public void logHours(WorkLogDTO request) {
        ToDoItem task = toDoItemRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        WorkLog workLog = new WorkLog();

        workLog.setWorkedHours(request.getWorkedHours());
        workLog.setWorkedDay(request.getWorkedDay());
        workLog.setCreatedAt(OffsetDateTime.now());

        // Fk
        workLog.setTaskId(task);
        workLog.setUserId(task.getUser());

        workLogRepository.save(workLog);
    }

    public List<WorkLog> findAll() {
        List<WorkLog> workLogs = workLogRepository.findAll();
        return workLogs;
    }

    public ResponseEntity<WorkLog> getItemById(int id) {
        Optional<WorkLog> workLogData = workLogRepository.findById(id);
        if (workLogData.isPresent()) {
            return new ResponseEntity<>(workLogData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public WorkLog addWorkLog(WorkLog workLog) {
        return workLogRepository.save(workLog);
    }

    public boolean deleteWorkLog(int id) {
        try {
            workLogRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WorkLog updateWorkLog(int id, WorkLog td) {
        Optional<WorkLog> workLogData = workLogRepository.findById(id);
        if (workLogData.isPresent()) {
            WorkLog workLogItem = workLogData.get();
            workLogItem.setWorkLogId(id);
            workLogItem.setUserId(td.getUserId());
            workLogItem.setTaskId(td.getTaskId());
            workLogItem.setWorkedDay(td.getWorkedDay());
            workLogItem.setWorkedHours(td.getWorkedHours());
            return workLogRepository.save(workLogItem);
        } else {
            return null;
        }
    }

}
