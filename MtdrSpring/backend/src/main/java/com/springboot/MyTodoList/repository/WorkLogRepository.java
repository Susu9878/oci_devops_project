package com.springboot.MyTodoList.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springboot.MyTodoList.model.WorkLog;

import jakarta.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface WorkLogRepository extends JpaRepository<WorkLog, Integer>{

    
} 
