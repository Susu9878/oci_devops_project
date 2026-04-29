package com.springboot.MyTodoList.repository;

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
            SUM(CASE WHEN t.STATUS = 'DONE' THEN 1 ELSE 0 END) AS completedTasks,
            COUNT(DISTINCT u.USER_ID) AS totalUsers,
            NVL(SUM(w.WORKED_HOURS), 0) AS totalHours
        FROM TODOITEM t
        JOIN USERS u ON t.USER_ID = u.USER_ID
        LEFT JOIN WORK_LOG w ON w.TASK_ID = t.ID
        WHERE t.SPRINT_ID = :sprintId
          AND u.TEAM_ID = :teamId
    """, nativeQuery = true)
    Object[] getTeamKpisRaw(int sprintId, int teamId);
} 
