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
            u.USER_ID,
            u.USERNAME,
            SUM(CASE WHEN t.STATUS = 'DONE' THEN 1 ELSE 0 END) AS completed,
            SUM(CASE WHEN t.STATUS = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS inProgress,
            SUM(CASE WHEN t.STATUS = 'NOT_STARTED' THEN 1 ELSE 0 END) AS notStarted,
            NVL(SUM(w.WORKED_HOURS), 0) AS hoursWorked
        FROM USERS u
            LEFT JOIN TODOITEM t 
                ON t.USER_ID = u.USER_ID 
                AND t.SPRINT_ID = :sprintId
            LEFT JOIN WORK_LOG w 
                ON w.USER_ID = u.USER_ID 
                AND w.TASK_ID = t.ID
        WHERE u.TEAM_ID = :teamId
        GROUP BY u.USER_ID, u.USERNAME
    """, nativeQuery = true)
    List<Object[]> getUserKpisRaw(int sprintId, int teamId);
}
