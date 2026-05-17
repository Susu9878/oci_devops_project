package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ToDoItemTest {

    @Mock
    private ToDoItemRepository toDoItemRepository;

    @InjectMocks
    private ToDoItemService toDoItemService;

    @Test
    void addToDoItem() {
        ToDoItem newTask = new ToDoItem(
                null,
                "Write unit test",
                "Verify task creation with Mockito",
                null,
                null,
                null,
                ToDoItem.TaskStatus.NOT_STARTED,
                OffsetDateTime.now(),
                null,
                null,
                null,
                null
        );

        ToDoItem savedTask = new ToDoItem(
                1,
                "Write unit test",
                "Verify task creation with Mockito",
                null,
                null,
                null,
                ToDoItem.TaskStatus.NOT_STARTED,
                newTask.getCreation_ts(),
                null,
                null,
                null,
                null
        );

        when(toDoItemRepository.save(any(ToDoItem.class))).thenReturn(savedTask);

        ToDoItem result = toDoItemService.addToDoItem(newTask);

        assertThat(result).isNotNull();
        assertThat(result.getTaskId()).isEqualTo(1);
        assertThat(result.getDescription()).isEqualTo("Verify task creation with Mockito");
        verify(toDoItemRepository).save(newTask);
    }
}
