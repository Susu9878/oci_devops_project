package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletedTasksTest {

    @Mock
    private ToDoItemRepository toDoItemRepository;

    @InjectMocks
    private ToDoItemService toDoItemService;

    @Test
    void getToDoSprintById() {
        ToDoItem task = new ToDoItem();
        task.setTaskId(1);
        task.setDescription("desc");
        task.setCreation_ts(OffsetDateTime.now());
        task.setSprintId(42);

        when(toDoItemRepository.findById(1)).thenReturn(Optional.of(task));

        Integer sprintId = toDoItemService.getToDoSprintById(1);

        assertThat(sprintId).isNotNull().isEqualTo(42);
        verify(toDoItemRepository).findById(1);
    }

    @Test
    void getToDoByUserIdAndSprintId() {
        ToDoItem task1 = new ToDoItem();
        task1.setTaskId(1);
        task1.setUserId(1);
        task1.setDescription("desc1");
        task1.setCreation_ts(OffsetDateTime.now());
        task1.setSprintId(42);

        ToDoItem task2 = new ToDoItem();
        task2.setTaskId(2);
        task2.setUserId(1);
        task2.setDescription("desc2");
        task2.setCreation_ts(OffsetDateTime.now());
        task2.setSprintId(42);

        List<ToDoItem> expectedTasks = List.of(task1, task2);

        when(toDoItemRepository.findByUserIdAndSprintId(1, 42)).thenReturn(expectedTasks);

        List<ToDoItem> tasks = toDoItemService.getToDoItemsByUserAndSprint(1, 42);

        assertThat(tasks).isNotNull().hasSize(2);
        assertThat(tasks).containsExactly(task1, task2);
        verify(toDoItemRepository).findByUserIdAndSprintId(1, 42);
    }
}

