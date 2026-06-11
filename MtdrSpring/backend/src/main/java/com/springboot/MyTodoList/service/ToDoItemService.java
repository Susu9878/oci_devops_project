package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.DTO.ToDoItemRequestDTO;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.repository.SprintRepository;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.springboot.MyTodoList.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SprintRepository sprintRepository;

    public List<ToDoItem> findAll() {
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        return todoItems;
    }

    public List<ToDoItem> findBySprint(int sprintId, String email) {
        System.out.println("JWT email = " + email);
        return toDoItemRepository.findAll()
                .stream()
                .peek(item -> {
                    System.out.println(
                            "Task: " + item.getTaskName()
                                    + " User: "
                                    + (item.getUser() != null
                                            ? item.getUser().getEmail()
                                            : "NULL"));
                })
                .filter(item -> item.getSprint().getSprintId() == sprintId)
                .filter(item -> item.getUser() != null)
                .filter(item -> item.getUser().getEmail().equals(email))
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

    // Filtering gets (?)

    public List<ToDoItem> findByStatus(ToDoItem.TaskStatus status) {
        return toDoItemRepository.findByStatus(status);
    }

    public List<ToDoItem> findByUserId(int userId) {
        return toDoItemRepository.findByUser_UserId(userId);
    }

    public List<ToDoItem> findActiveByUserAndSprint(int userId, int sprintId) {
        return toDoItemRepository.findActiveTasksByUserAndSprint(userId, sprintId);
    }

    // MUTATIONS

    public ToDoItem addToDoItem(ToDoItemRequestDTO dto) {
        ToDoItem item = dto.toEntity();

        item.setCreatedAt(OffsetDateTime.now());
        if (item.getStatus() == null)
            item.setStatus(ToDoItem.TaskStatus.NOT_STARTED);
        if (item.getPriority() == null)
            item.setPriority(ToDoItem.TaskPriority.MEDIUM);

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getUserId()));
            item.setUser(user);
        }
        if (dto.getSprintId() != null) {
            Sprint sprint = sprintRepository.findById(dto.getSprintId())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + dto.getSprintId()));
            item.setSprint(sprint);
        }

        return toDoItemRepository.save(item);
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

    public ToDoItem updateToDoItem(int id, ToDoItemRequestDTO dto) {
        ToDoItem item = toDoItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        if (dto.getTaskName() != null)
            item.setTaskName(dto.getTaskName());
        if (dto.getDescription() != null)
            item.setDescription(dto.getDescription());
        if (dto.getStoryPoints() != null)
            item.setStoryPoints(dto.getStoryPoints());
        if (dto.getExpectedHours() != null)
            item.setExpectedHours(dto.getExpectedHours());
        if (dto.getPriority() != null)
            item.setPriority(ToDoItem.TaskPriority.valueOf(dto.getPriority()));
        if (dto.getStatus() != null)
            item.setStatus(ToDoItem.TaskStatus.valueOf(dto.getStatus()));

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getUserId()));
            item.setUser(user);
        }
        if (dto.getSprintId() != null) {
            Sprint sprint = sprintRepository.findById(dto.getSprintId())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + dto.getSprintId()));
            item.setSprint(sprint);
        }

        return toDoItemRepository.save(item);
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
