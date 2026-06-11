package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.DTO.ToDoItemRequestDTO;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.security.JwtUtil;
import com.springboot.MyTodoList.service.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoItemController {

    @Autowired
    private ToDoItemService toDoItemService;

    private final JwtUtil jwtUtil;

    public ToDoItemController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /*
     * GETS all tasks
     * [
     * {
     * taskId: num,
     * taskName: String,
     * description: String,
     * storyPoints: num,
     * expectedHours: num,
     * priority: "LOWEST" | "LOW" | "MEDIUM" | "HIGH" | "CRITICAL",
     * status: "NOT_STARTED" | "IN_PROGRESS" | "DONE" | "NOT_DONE",
     * createdAt: DateTime,
     * startDate: DateTime,
     * completionDate: DateTime,
     * user: { userId, userName, ... },
     * sprint: { sprintId, sprintName, ... }
     * }
     * ]
     */

    // @CrossOrigin
    @GetMapping(value = "/todolist")
    public List<ToDoItem> getAllToDoItems() {
        return toDoItemService.findAll();
    }

    /*
     * GETS all tasks for a given sprint
     * query param: sprintId (int)
     * returns same shape as GET /todolist
     */
    @GetMapping(value = "/todolist/sprint")
    public List<ToDoItem> getToDoItemsBySprint(@RequestParam int sprintId,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String email = jwtUtil.extractUsername(token);

        return toDoItemService.findBySprint(sprintId, email);
    }

    // @CrossOrigin
    /*
     * GETS a single task by id
     * path param: id (int)
     * returns single task object or 404 if not found
     */
    @GetMapping(value = "/todolist/{id}")
    public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
        try {
            ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
            return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin

    /*
     * GETS all tasks filtered by status
     * path param: status — one of "NOT_STARTED" | "IN_PROGRESS" | "DONE" |
     * "NOT_DONE"
     * returns same shape as GET /todolist
     * 400 if status value is invalid
     */
    @GetMapping("/todolist/status/{status}")
    public ResponseEntity<List<ToDoItem>> getToDoItemsByStatus(@PathVariable ToDoItem.TaskStatus status) {
        try {
            return new ResponseEntity<>(toDoItemService.findByStatus(status), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET by assigned user
    /*
     * GETS all tasks assigned to a user
     * path param: userId (int)
     * returns same shape as GET /todolist
     */
    @GetMapping("/todolist/user/{userId}")
    public ResponseEntity<List<ToDoItem>> getToDoItemsByUser(@PathVariable int userId) {
        try {
            return new ResponseEntity<>(toDoItemService.findByUserId(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Change these three signatures:
    /*
     * CREATES a new task
     * request body:
     * {
     * taskName: String, (required)
     * description: String, (required)
     * storyPoints: num,
     * expectedHours: num,
     * priority: "LOWEST" | "LOW" | "MEDIUM" | "HIGH" | "CRITICAL", (default:
     * MEDIUM)
     * status: "NOT_STARTED" | "IN_PROGRESS" | "DONE" | "NOT_DONE", (default:
     * NOT_STARTED)
     * userId: num,
     * sprintId: num
     * }
     * returns 200 with location header set to the new taskId
     * 400 if userId or sprintId don't exist
     */
    @PostMapping("/todolist")
    public ResponseEntity<ToDoItem> addToDoItem(@RequestBody ToDoItemRequestDTO dto) {
        try {
            ToDoItem created = toDoItemService.addToDoItem(dto);
            HttpHeaders headers = new HttpHeaders();
            headers.set("location", "" + created.getTaskId());
            headers.set("Access-Control-Expose-Headers", "location");
            return ResponseEntity.ok().headers(headers).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // fix PUT
    /*
     * UPDATES an existing task by id
     * path param: id (int)
     * request body (all fields optional, only sent fields are updated):
     * {
     * taskName: String,
     * description: String,
     * storyPoints: num,
     * expectedHours: num,
     * priority: "LOWEST" | "LOW" | "MEDIUM" | "HIGH" | "CRITICAL",
     * status: "NOT_STARTED" | "IN_PROGRESS" | "DONE" | "NOT_DONE",
     * userId: num,
     * sprintId: num
     * }
     * returns updated task or 404 if not found
     */
    @PutMapping("/todolist/{id}")
    public ResponseEntity<ToDoItem> updateToDoItem(@RequestBody ToDoItemRequestDTO dto, @PathVariable int id) {
        try {
            ToDoItem updated = toDoItemService.updateToDoItem(id, dto); // remove .toEntity()
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET methods return ToDoItemResponseDTO instead of ToDoItem — update return
    // types accordingly
    // @CrossOrigin

    /*
     * DELETES a task by id
     * path param: id (int)
     * returns true on success, 404 if not found
     */
    @DeleteMapping("/todolist/{id}")
    public ResponseEntity<Boolean> deleteToDoItem(@PathVariable int id) {
        try {
            return new ResponseEntity<>(toDoItemService.deleteToDoItem(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}