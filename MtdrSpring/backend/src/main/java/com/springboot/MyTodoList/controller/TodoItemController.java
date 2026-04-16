package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TodoItem;
import com.springboot.MyTodoList.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoItemController {
    @Autowired
    private TodoItemService todoItemService;
    //@CrossOrigin
    @GetMapping(value = "/todolist")
    public List<TodoItem> getAllTodoItems(){
        return todoItemService.findAll();
    }
    //@CrossOrigin
    @GetMapping(value = "/todolist/{id}")
    public ResponseEntity<TodoItem> getTodoItemById(@PathVariable int id){
        try{
            ResponseEntity<TodoItem> responseEntity = todoItemService.getItemById(id);
            return new ResponseEntity<TodoItem>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //@CrossOrigin
    @PostMapping(value = "/todolist")
    public ResponseEntity<TodoItem> addTodoItem(@RequestBody TodoItem todoItem) throws Exception{
        TodoItem td = todoItemService.addTodoItem(todoItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location",""+td.getID());
        responseHeaders.set("Access-Control-Expose-Headers","location");
        //URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }
    //@CrossOrigin
    @PutMapping(value = "/todolist/{id}")
    public ResponseEntity<TodoItem> updateTosoItem(@RequestBody TodoItem todoItem, @PathVariable int id){
        try{
            TodoItem todoItem1 = todoItemService.updateTodoItem(id, todoItem);
            System.out.println(todoItem1.toString());
            return new ResponseEntity<>(todoItem1,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //@CrossOrigin
    @DeleteMapping(value = "/todolist/{id}")
    public ResponseEntity<Boolean> deleteTodoItem(@PathVariable("id") int id){
        Boolean flag = false;
        try{
            flag = todoItemService.deleteTodoItem(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(flag,HttpStatus.NOT_FOUND);
        }
    }
}
