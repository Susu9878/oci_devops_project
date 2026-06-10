package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    public List<ToDoItem> findAll() {
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        return todoItems;
    }

    public List<ToDoItem> findBySprint(int sprintId) {
        return toDoItemRepository.findAll()
                .stream()
                .filter(item -> item.getSprint().getSprintId() == sprintId)
                .toList();
    }

    public ResponseEntity<ToDoItem> getItemById(int id) {
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()) {
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ToDoItem getToDoItemById(int id) {
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()) {
            return todoData.get();
        } else {
            return null;
        }
    }

    public ToDoItem addToDoItem(ToDoItem toDoItem) {
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
            // Main task info
            toDoItem.setTaskName(td.getTaskName());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setStatus(td.getStatus());
            toDoItem.setStartDate(td.getStartDate());
            toDoItem.setCompletionDate(td.getCompletionDate());
            // Extra
            toDoItem.setStoryPoints(td.getStoryPoints());
            toDoItem.setExpectedHours(td.getExpectedHours());
            toDoItem.setPriority(td.getPriority());
            // FK
            toDoItem.setSprint(td.getSprint());
            toDoItem.setUser(td.getUser());

            return toDoItemRepository.save(toDoItem);
        } else {
            return null;
        }
    }

    public List<ToDoItem> findTasksByUserAndActiveSprint(int userId) {
        return findAll().stream()
                .filter(task -> task.getUser() != null)
                .filter(task -> task.getSprint() != null)
                .filter(task -> task.getUser().getUserId() == userId)
                .filter(task -> task.getSprint().getStatus() == Sprint.SprintStatus.ACTIVE)
                .toList();
    }
}
