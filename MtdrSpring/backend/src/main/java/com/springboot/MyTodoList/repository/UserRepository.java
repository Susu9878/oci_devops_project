package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface UserRepository extends JpaRepository<User, Integer> {
    /*
     * this includes
     * save()
     * findById()
     * findAll()
     * delete()
     * pagination, sorting, etc.
     */

    // Authentication
    Optional<User> findByUsername(String username);

    // Auth
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // originall. was in todoitemrepository
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
    List<Object[]> getUserKpisPerSprint(int sprintId, int teamId);

    @Query(value = """
            SELECT
                u.ID,
                u.USERNAME,

                COUNT(DISTINCT t.ID) AS totalTasksAssigned,

                NVL(SUM(
                    CASE WHEN t.STATUS = 'DONE'
                    THEN 1
                    ELSE 0
                    END
                ), 0) AS totalTasksCompleted,

                NVL((
                    SELECT SUM(w.WORKED_HOURS)
                    FROM WORK_LOG w
                    WHERE w.USER_ID = u.ID
                ), 0) AS totalHoursWorked,

                COUNT(DISTINCT t.SPRINT_ID) AS sprintCount

            FROM USERS u
            LEFT JOIN TODOITEM t
                ON t.USER_ID = u.ID

            WHERE u.ID = :userId
              AND u.TEAM_ID = :teamId

            GROUP BY u.ID, u.USERNAME
                        """, nativeQuery = true)
    List<Object[]> getUserKpisAllSprints(int userId, int teamId);

    // Graph 1
    @Query(value = """
                SELECT
                    t.SPRINT_ID,
                    u.USERNAME,
                    SUM(CASE WHEN t.STATUS = 'DONE' THEN 1 ELSE 0 END) AS completedTasks
                FROM USERS u
                LEFT JOIN TODOITEM t
                    ON t.USER_ID = u.ID
                WHERE u.TEAM_ID = :teamId
                GROUP BY t.SPRINT_ID, u.USERNAME
                ORDER BY t.SPRINT_ID, u.USERNAME
            """, nativeQuery = true)
    List<Object[]> getCompletedTasksPerUserPerSprint(int teamId);

    // graph 2 week 5 presentation
    @Query(value = """
                SELECT
                    t.SPRINT_ID,
                    u.USERNAME,
                    NVL(SUM(w.WORKED_HOURS), 0) AS totalHours
                FROM USERS u
                LEFT JOIN TODOITEM t
                    ON t.USER_ID = u.ID
                LEFT JOIN WORK_LOG w
                    ON w.TASK_ID = t.ID AND w.USER_ID = u.ID
                WHERE u.TEAM_ID = :teamId
                GROUP BY t.SPRINT_ID, u.USERNAME
                ORDER BY t.SPRINT_ID, u.USERNAME
            """, nativeQuery = true)
    List<Object[]> getHoursPerUserPerSprint(int teamId);
}