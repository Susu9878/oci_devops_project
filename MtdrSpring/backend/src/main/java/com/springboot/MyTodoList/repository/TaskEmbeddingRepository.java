package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.TaskEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskEmbeddingRepository
        extends JpaRepository<TaskEmbedding,Integer> {
}