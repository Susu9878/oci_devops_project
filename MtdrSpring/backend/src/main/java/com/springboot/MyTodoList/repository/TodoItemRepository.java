package com.springboot.MyTodoList.repository;


import com.springboot.MyTodoList.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface TodoItemRepository extends JpaRepository<TodoItem,Integer> {


}
