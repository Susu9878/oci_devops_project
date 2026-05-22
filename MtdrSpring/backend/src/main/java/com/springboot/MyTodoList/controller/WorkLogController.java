package com.springboot.MyTodoList.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.DTO.WorkLogDTO;
import com.springboot.MyTodoList.service.WorkLogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/todolist/worklogs")
@CrossOrigin
public class WorkLogController {
    @Autowired
    private WorkLogService workLogService;

    /*
     * request body example
     * {
     * "workedHours": 6.5,
     * "workedDay": "2026-05-21T00:00:00Z",
     * "taskId": 12,
     * "userId": 4
     * }
     */
    @PostMapping
    public ResponseEntity<Void> logHours(@RequestBody WorkLogDTO workLogDTO) {
        workLogService.logHours(workLogDTO);
        return ResponseEntity.ok().build();
    }

}
