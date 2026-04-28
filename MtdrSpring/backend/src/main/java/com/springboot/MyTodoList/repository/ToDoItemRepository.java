package com.springboot.MyTodoList.repository;


import com.springboot.MyTodoList.model.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface ToDoItemRepository extends JpaRepository<ToDoItem,Integer> {
/*
this include 
save()
findById()
findAll()
delete()
pagination, sorting, etc.
*/

}
