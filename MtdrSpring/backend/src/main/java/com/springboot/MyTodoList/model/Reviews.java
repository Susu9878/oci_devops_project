package com.springboot.MyTodoList.model;

import jakarta.persistence.*;

@Entity
@Table(name = "REVIEWS")
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int reviewId;

    @Column(name = "REVIEW_STATUS")
    String reviewStatus;

    @Column(name = "COMMENT")
    String comment;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "TASK_ID")
    private Integer taskId;

    public Reviews(){}

    public Reviews(int reviewId, String reviewStatus, String comment, Integer userId, Integer taskId){
        this.reviewId = reviewId;
        this.reviewStatus = reviewStatus;
        this.comment = comment;
        this.userId = userId;
        this.taskId = taskId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
    
}
