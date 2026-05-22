package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.service.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoItemController {
    @Autowired
    private ToDoItemService toDoItemService;
    //@CrossOrigin
    @GetMapping(value = "/todolist")
    public ResponseEntity<List<ToDoItem>> getAllToDoItems() {
        List<ToDoItem> items = toDoItemService.findAll();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    //@CrossOrigin
    @GetMapping(value = "/todolist/{id}")
    public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id){
        try{
            ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
            return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET by status
    @GetMapping(value = "/todolist/status/{status}")
    public ResponseEntity<List<ToDoItem>> getToDoItemsByStatus(
            @PathVariable ToDoItem.TaskStatus status) {
        try {
            List<ToDoItem> items = toDoItemService.findByStatus(status);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // fired when the path value doesn't match any enum constant
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET by assigned user
    @GetMapping(value = "/todolist/user/{userId}")
    public ResponseEntity<List<ToDoItem>> getToDoItemsByUser(@PathVariable int userId) {
        try {
            List<ToDoItem> items = toDoItemService.findByUserId(userId);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //GET BY SPRINT 2
    @GetMapping(value = "/todolist/sprint/{sprintId}")
    public ResponseEntity<List<ToDoItem>> getToDoItemsBySprint(@PathVariable int sprintId) {
        try {
            List<ToDoItem> items = toDoItemService.findBySprintId(sprintId);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET BY SPRINT 2 USER 3
    @GetMapping(value = "/todolist/user/{userId}/sprint/{sprintId}/active")
    public ResponseEntity<List<ToDoItem>> getActiveByUserAndSprint(
            @PathVariable int userId,
            @PathVariable int sprintId) {
        try {
            List<ToDoItem> items = toDoItemService.findActiveByUserAndSprint(userId, sprintId);
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //@CrossOrigin
    @PostMapping(value = "/todolist")
    public ResponseEntity<ToDoItem> addToDoItem(@RequestBody ToDoItem todoItem) throws Exception{
        ToDoItem td = toDoItemService.addToDoItem(todoItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location",""+td.getTaskId());
        responseHeaders.set("Access-Control-Expose-Headers","location");
        //URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }
    //@CrossOrigin
    @PutMapping(value = "todolist/{id}")
    public ResponseEntity<ToDoItem> updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id){
        try{
            ToDoItem toDoItem1 = toDoItemService.updateToDoItem(id, toDoItem);
            System.out.println(toDoItem1.toString());
            return new ResponseEntity<>(toDoItem1,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //@CrossOrigin
    @DeleteMapping(value = "todolist/{id}")
    public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id){
        Boolean flag = false;
        try{
            flag = toDoItemService.deleteToDoItem(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(flag,HttpStatus.NOT_FOUND);
        }
    }



}