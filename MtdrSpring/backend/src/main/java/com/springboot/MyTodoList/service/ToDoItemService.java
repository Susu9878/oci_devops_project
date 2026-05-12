package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    public List<ToDoItem> findAll(){
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        return todoItems;
    }
    public ResponseEntity<ToDoItem> getItemById(int id){
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()){
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ToDoItem getToDoItemById(int id){
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()){
            return todoData.get();
        }else{
            return null;
        }
    }

    // Filteringf gets (?)


    public List<ToDoItem> findByStatus(ToDoItem.TaskStatus status) {
        return toDoItemRepository.findByStatus(status);
    }

    public List<ToDoItem> findByUserId(int userId) {
        return toDoItemRepository.findByUser_UserId(userId);
    }

    public List<ToDoItem> findBySprintId(int sprintId) {
        return toDoItemRepository.findBySprint_SprintId(sprintId);
    }

    public List<ToDoItem> findActiveByUserAndSprint(int userId, int sprintId) {
        return toDoItemRepository.findActiveTasksByUserAndSprint(userId, sprintId);
    }

    //MUTATIONS


    public ToDoItem addToDoItem(ToDoItem toDoItem) {
        if (toDoItem.getCreation_ts() == null) {
            toDoItem.setCreation_ts(OffsetDateTime.now());
        }
        if (toDoItem.getStatus() == null) {
            toDoItem.setStatus(ToDoItem.TaskStatus.NOT_STARTED);
        }
        if (toDoItem.getPriority() == null) {
            toDoItem.setPriority(ToDoItem.TaskPriority.MEDIUM);
        }
        return toDoItemRepository.save(toDoItem);
    }


    public boolean deleteToDoItem(int id) {
        try {
            toDoItemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public ToDoItem updateToDoItem(int id, ToDoItem td) {
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if (toDoItemData.isPresent()) {
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setTaskId(id);
            toDoItem.setTaskName(td.getTaskName());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setStoryPoints(td.getStoryPoints());
            toDoItem.setExpectedHours(td.getExpectedHours());
            toDoItem.setPriority(td.getPriority());
            toDoItem.setStatus(td.getStatus());
            toDoItem.setCreation_ts(td.getCreation_ts());
            toDoItem.setStartDate(td.getStartDate());
            toDoItem.setCompletionDate(td.getCompletionDate());
            toDoItem.setDone(td.isDone());
            return toDoItemRepository.save(toDoItem);
        } else {
            return null;
        }
    }
}