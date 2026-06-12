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

    @Query("""
    SELECT t
    FROM ToDoItem t
    WHERE t.user.userId = :userId
    AND t.sprint.sprintId = :sprintId
    AND t.status != com.springboot.MyTodoList.model.ToDoItem.TaskStatus.DONE
""")
    List<ToDoItem> findActiveTasksByUserAndSprint(
            int userId,
            int sprintId);

    @Query("""
    SELECT t
    FROM ToDoItem t
    WHERE t.user.userId = :userId
    AND t.sprint.sprintId = :sprintId
    AND t.status = com.springboot.MyTodoList.model.ToDoItem.TaskStatus.NOT_STARTED
""")
    List<ToDoItem> findNotStartedTasksByUserAndSprint(
            int userId,
            int sprintId);

    @Query("""
SELECT t
FROM ToDoItem t
WHERE t.user.userId = :userId
AND t.sprint.sprintId = :sprintId
AND t.status != com.springboot.MyTodoList.model.ToDoItem.TaskStatus.DONE
""")
    List<ToDoItem> findRoadmapTasksByUserAndSprint(
            int userId,
            int sprintId);

    @Query("""
SELECT t
FROM ToDoItem t
WHERE t.status = com.springboot.MyTodoList.model.ToDoItem.TaskStatus.DONE
AND t.priority = :priority
AND t.storyPoints IS NOT NULL
AND ABS(t.storyPoints - :storyPoints) <= 2
""")
    List<ToDoItem> findSimilarCompletedTasks(
            ToDoItem.TaskPriority priority,
            Integer storyPoints);

    List<ToDoItem> findByStatus(ToDoItem.TaskStatus status);

    List<ToDoItem> findByUser_UserId(int userId);

    List<ToDoItem> findBySprint_SprintId(int sprintId);
}