package com.example.MyTodoList.controller;

import com.example.MyTodoList.service.KPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kpi")
@CrossOrigin(origins = "*")
public class KPIController {

    @Autowired
    private KPIService kpiService;

    @GetMapping("/completed-tasks")
    public ResponseEntity<Map<String, Object>> getCompletedTasks(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        int total = kpiService.getTotalCompletedTasks(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("kpi", "Total Completed Tasks");
        response.put("value", total);
        if (startDate != null) response.put("startDate", startDate);
        if (endDate != null) response.put("endDate", endDate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/real-hours")
    public ResponseEntity<Map<String, Object>> getRealHoursWorked(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        double totalHours = kpiService.getTotalRealHoursWorked(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("kpi", "Total Real Hours Worked");
        response.put("value", totalHours);
        if (startDate != null) response.put("startDate", startDate);
        if (endDate != null) response.put("endDate", endDate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/avg-tasks-per-developer")
    public ResponseEntity<Map<String, Object>> getAvgTasksPerDeveloper(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        List<Map<String, Object>> developers = kpiService.getAvgCompletedTasksPerDeveloper(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("kpi", "Average Completed Tasks Per Developer");
        response.put("developers", developers);
        if (startDate != null) response.put("startDate", startDate);
        if (endDate != null) response.put("endDate", endDate);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/avg-hours-per-developer")
    public ResponseEntity<Map<String, Object>> getAvgHoursPerDeveloper(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        List<Map<String, Object>> developers = kpiService.getAvgHoursWorkedPerDeveloper(startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("kpi", "Average Hours Worked Per Developer");
        response.put("developers", developers);
        if (startDate != null) response.put("startDate", startDate);
        if (endDate != null) response.put("endDate", endDate);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getKPISummary(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalCompletedTasks", kpiService.getTotalCompletedTasks(startDate, endDate));
        summary.put("totalRealHoursWorked", kpiService.getTotalRealHoursWorked(startDate, endDate));
        summary.put("avgTasksPerDeveloper", kpiService.getAvgCompletedTasksPerDeveloper(startDate, endDate));
        summary.put("avgHoursPerDeveloper", kpiService.getAvgHoursWorkedPerDeveloper(startDate, endDate));
        if (startDate != null) summary.put("startDate", startDate);
        if (endDate != null) summary.put("endDate", endDate);

        return ResponseEntity.ok(summary);
    }
}