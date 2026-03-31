package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.TaskItem;
import com.springboot.MyTodoList.repository.TaskItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class TaskItemService {

    @Autowired
    private TaskItemRepository taskItemRepository;
    public List<TaskItem> findAll(){
        List<TaskItem> taskItems = taskItemRepository.findAll();
        return taskItems;
    }
    public ResponseEntity<TaskItem> getItemById(int id){
        Optional<TaskItem> taskData = taskItemRepository.findById(id);
        if (taskData.isPresent()){
            return new ResponseEntity<>(taskData.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public TaskItem getTaskItemById(int id){
        Optional<TaskItem> taskData = taskItemRepository.findById(id);
        if (taskData.isPresent()){
            return taskData.get();
        }else{
            return null;
        }
    }

    
    public TaskItem addTaskItem(TaskItem taskItem){
        return taskItemRepository.save(taskItem);
    }

    public boolean deleteTaskItem(int id){
        try{
            taskItemRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public TaskItem updateTaskItem(int id, TaskItem td){
        Optional<TaskItem> taskItemData = taskItemRepository.findById(id);
        if(taskItemData.isPresent()){
            TaskItem taskItem = taskItemData.get();
            taskItem.setID(id);
            taskItem.setCreation_ts(td.getCreation_ts());
            taskItem.setDescription(td.getDescription());
            taskItem.setDone(td.isDone());
            return taskItemRepository.save(taskItem);
        }else{
            return null;
        }
    }
    

}
