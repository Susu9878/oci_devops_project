package com.springboot.MyTodoList.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TASK_EMBEDDINGS")
public class TaskEmbedding {

    @Id
    @Column(name = "TASK_ID")
    private Integer taskId;

    @Lob
    @Column(name = "EMBEDDING_JSON")
    private String embeddingJson;

    // getters/setters
}