package com.springboot.MyTodoList.model;

import jakarta.persistence.*;

@Entity
@Table(name = "REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    int reviewId;
    @Column(name = "REVIEW_STATUS")//TODO not nullable, constraints
    String reviewStatus;
    @Column(name = "COMMENT")
    String comment;
    //FK
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private ToDoItem task;

    public Review(){}

    public Review(int reviewId, String reviewStatus, String comment, User userId, ToDoItem taskId){
        this.reviewId = reviewId;
        this.reviewStatus = reviewStatus;
        this.comment = comment;
        this.user = userId;
        this.task = taskId;
    }

    public int getReviewId(){
        return reviewId;
    }

    public void setReviewId(int reviewId){
        this.reviewId = reviewId;
    }

    public String getReviewStatus(){
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus){
        this.reviewStatus = reviewStatus;
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = userId;
    }

    public ToDoItem getTask() {
        return task;
    }

    public void setTask(ToDoItem taskId) {
        this.task = taskId;
    }
    
}