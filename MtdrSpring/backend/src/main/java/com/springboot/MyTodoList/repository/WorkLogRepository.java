package com.springboot.MyTodoList.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springboot.MyTodoList.model.WorkLog;

import jakarta.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface WorkLogRepository extends JpaRepository<WorkLog, Integer>{

    @Query(value = """
    SELECT 
        COUNT(DISTINCT t.ID) AS totalTasks,
        NVL(SUM(CASE WHEN t.STATUS = 'DONE' THEN 1 ELSE 0 END), 0) AS completedTasks,
        COUNT(DISTINCT u.ID) AS totalUsers,
        NVL(SUM(w.WORKED_HOURS), 0) AS totalHours
    FROM USERS u
    LEFT JOIN TODOITEM t 
        ON t.USER_ID = u.ID 
        AND t.SPRINT_ID = :sprintId
    LEFT JOIN WORK_LOG w ON w.TASK_ID = t.ID
    WHERE u.TEAM_ID = :teamId
    """, nativeQuery = true)
    List<Object[]> getTeamKpisRaw(int sprintId, int teamId);
} 
