package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.TodoItem;
import com.springboot.MyTodoList.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;
    public List<TodoItem> findAll(){
        List<TodoItem> todoItems = todoItemRepository.findAll();
        return todoItems;
    }
    public ResponseEntity<TodoItem> getItemById(int id){
        Optional<TodoItem> todoData = todoItemRepository.findById(id);
        if (todoData.isPresent()){
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public TodoItem getTodoItemById(int id){
        Optional<TodoItem> todoData = todoItemRepository.findById(id);
        if (todoData.isPresent()){
            return todoData.get();
        }else{
            return null;
        }
    }

    
    public TodoItem addTodoItem(TodoItem todoItem){
        return todoItemRepository.save(todoItem);
    }

    public boolean deleteTodoItem(int id){
        try{
            todoItemRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public TodoItem updateTodoItem(int id, TodoItem td){
        Optional<TodoItem> todoItemData = todoItemRepository.findById(id);
        if(todoItemData.isPresent()){
            TodoItem todoItem = todoItemData.get();
            todoItem.setID(id);
            todoItem.setCreation_ts(td.getCreation_ts());
            todoItem.setDescription(td.getDescription());
            todoItem.setDone(td.isDone());
            return todoItemRepository.save(todoItem);
        }else{
            return null;
        }
    }
    

}
