package com.springboot.MyTodoList.controller;
import com.springboot.MyTodoList.model.TaskItem;
import com.springboot.MyTodoList.service.TaskItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskItemController {
    @Autowired
    private TaskItemService taskItemService;
    //@CrossOrigin
    @GetMapping(value = "/tasklist")
    public List<TaskItem> getAllTaskItems(){
        return taskItemService.findAll();
    }
    //@CrossOrigin
    @GetMapping(value = "/tasklist/{id}")
    public ResponseEntity<TaskItem> getTaskItemById(@PathVariable int id){
        try{
            ResponseEntity<TaskItem> responseEntity = taskItemService.getItemById(id);
            return new ResponseEntity<TaskItem>(responseEntity.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //@CrossOrigin
    @PostMapping(value = "/tasklist")
    public ResponseEntity<TaskItem> addTaskItem(@RequestBody TaskItem taskItem) throws Exception{
        TaskItem td = taskItemService.addTaskItem(taskItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location",""+td.getID());
        responseHeaders.set("Access-Control-Expose-Headers","location");
        //URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }
    //@CrossOrigin
    @PutMapping(value = "/tasklist/{id}")
    public ResponseEntity<TaskItem> updateTaskItem(@RequestBody TaskItem taskItem, @PathVariable int id){
        try{
            TaskItem taskItem1 = taskItemService.updateTaskItem(id, taskItem);
            System.out.println(taskItem1.toString());
            return new ResponseEntity<>(taskItem1,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    //@CrossOrigin
    @DeleteMapping(value = "/tasklist/{id}")
    public ResponseEntity<Boolean> deleteTaskItem(@PathVariable("id") int id){
        Boolean flag = false;
        try{
            flag = taskItemService.deleteTaskItem(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(flag,HttpStatus.NOT_FOUND);
        }
    }
}
