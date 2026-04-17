package com.example.MyTodoList.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KPIService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int getTotalCompletedTasks() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM TASK WHERE status = 'COMPLETED'",
                Integer.class
        );
    }

    public double getTotalRealHoursWorked() {
        return jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(worked_hours), 0) FROM WORK_LOG",
                Double.class
        );
    }

    public List<Map<String, Object>> getCompletedTasksPerDeveloper() {
        return jdbcTemplate.queryForList("""
            SELECT u.user_id, u.username, COUNT(t.task_id) AS completed_tasks
            FROM USER u
            LEFT JOIN TASK t ON u.user_id = t.user_id AND t.status = 'COMPLETED'
            GROUP BY u.user_id, u.username
            ORDER BY completed_tasks DESC
        """);
    }

    public List<Map<String, Object>> getHoursWorkedPerDeveloper() {
        return jdbcTemplate.queryForList("""
            SELECT u.user_id, u.username, COALESCE(SUM(wl.worked_hours), 0) AS total_hours
            FROM USER u
            LEFT JOIN WORK_LOG wl ON u.user_id = wl.user_id
            GROUP BY u.user_id, u.username
            ORDER BY total_hours DESC
        """);
    }
}