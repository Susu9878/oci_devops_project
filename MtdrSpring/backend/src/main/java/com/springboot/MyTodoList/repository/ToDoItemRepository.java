package com.springboot.MyTodoList.repository;


import com.springboot.MyTodoList.model.ToDoItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface ToDoItemRepository extends JpaRepository<ToDoItem,Integer> {
    /*
    this include 
    save()
    findById()
    findAll()
    delete()
    pagination, sorting, etc.
    */

    @Query(value = """
        SELECT 
            u.ID,
            u.USERNAME,
            NVL(SUM(CASE WHEN t.STATUS = 'DONE' THEN 1 ELSE 0 END), 0) AS completed,
            NVL(SUM(CASE WHEN t.STATUS = 'IN_PROGRESS' THEN 1 ELSE 0 END), 0) AS inProgress,
            NVL(SUM(CASE WHEN t.STATUS = 'NOT_STARTED' THEN 1 ELSE 0 END), 0) AS notStarted,
            NVL(SUM(CASE WHEN t.STATUS = 'NOT_DONE' THEN 1 ELSE 0 END), 0) AS notDone,
            NVL((
                SELECT SUM(w.WORKED_HOURS)
                FROM TODOUSER.WORK_LOG w
                JOIN TODOUSER.TODOITEM t2 ON w.TASK_ID = t2.ID
                WHERE w.USER_ID = u.ID
                AND t2.SPRINT_ID = :sprintId   
            ), 0) AS hoursWorked
        FROM TODOUSER.USERS u
        LEFT JOIN TODOUSER.TODOITEM t 
            ON t.USER_ID = u.ID 
            AND t.SPRINT_ID = :sprintId
        WHERE u.TEAM_ID = :teamId
        GROUP BY u.ID, u.USERNAME
    """, nativeQuery = true)
    List<Object[]> getUserKpisRaw(int sprintId, int teamId);
}
