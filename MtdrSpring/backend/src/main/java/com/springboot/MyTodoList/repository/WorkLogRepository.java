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

    //original
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
    List<Object[]> getTeamKpisPerSprint(int sprintId, int teamId);

    //same as query before but for all sprints
    @Query(value = """
    SELECT 
        COUNT(DISTINCT t.ID) AS totalTasks,
        NVL(SUM(CASE WHEN t.STATUS = 'DONE' THEN 1 ELSE 0 END), 0) AS completedTasks,
        COUNT(DISTINCT u.ID) AS totalUsers,
        NVL(SUM(w.WORKED_HOURS), 0) AS totalHours
    FROM USERS u
    LEFT JOIN TODOITEM t 
        ON t.USER_ID = u.ID 
    LEFT JOIN WORK_LOG w 
        ON w.TASK_ID = t.ID
    WHERE u.TEAM_ID = :teamId
    """, nativeQuery = true)
    List<Object[]> getTeamKpisAllSprints(int teamId);

    //graph 1 week 5 presentation
    @Query(value = """
    SELECT 
        t.SPRINT_ID,
        u.ID AS userId,
        u.USERNAME,
        COUNT(CASE WHEN t.STATUS = 'DONE' THEN 1 END) AS completedTasks
    FROM TODOITEM t
    JOIN USERS u ON t.USER_ID = u.ID
    WHERE u.TEAM_ID = :teamId
    GROUP BY t.SPRINT_ID, u.ID, u.USERNAME
    ORDER BY t.SPRINT_ID, u.ID
    """, nativeQuery = true)
    List<Object[]> getCompletedTasksPerUserPerSprint(int teamId);

    //graph 2 week 5
    @Query(value = """
    SELECT 
        s.SPRINT_ID,
        u.ID AS userId,
        u.USERNAME,
        NVL(h.totalHours, 0) AS totalHours
    FROM USERS u

    CROSS JOIN (
        SELECT DISTINCT t.SPRINT_ID
        FROM TODOITEM t
        JOIN USERS u2 ON u2.ID = t.USER_ID
        WHERE u2.TEAM_ID = :teamId
    ) s
    LEFT JOIN (
        SELECT 
            t.SPRINT_ID,
            w.USER_ID,
            SUM(w.WORKED_HOURS) AS totalHours
        FROM WORK_LOG w
        JOIN TODOITEM t 
            ON t.ID = w.TASK_ID
        GROUP BY t.SPRINT_ID, w.USER_ID
    ) h
        ON h.SPRINT_ID = s.SPRINT_ID
        AND h.USER_ID = u.ID
    WHERE u.TEAM_ID = :teamId
    ORDER BY s.SPRINT_ID, u.ID
    """, nativeQuery = true)
    List<Object[]> getHoursPerUserPerSprint(int teamId);
} 
