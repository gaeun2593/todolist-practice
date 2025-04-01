package com.todolist.domain.task.model.dto;

import com.todolist.domain.task.model.TaskStatus;

import java.time.LocalDateTime;

public class TodoAndCategoryDto {
    private int taskId;
    private int categoryId;
    private String userNickname;
    private String categoryTitle;
    private String taskContents;
    private LocalDateTime createdAt;
    private TaskStatus status;

    public TodoAndCategoryDto(int taskId, int categoryId, String userNickname, String categoryTitle, String taskContents, LocalDateTime createdAt, TaskStatus status) {
        this.taskId = taskId;
        this.categoryId = categoryId;
        this.userNickname = userNickname;
        this.categoryTitle = categoryTitle;
        this.taskContents = taskContents;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getTaskContents() {
        return taskContents;
    }

    public void setTaskContents(String taskContents) {
        this.taskContents = taskContents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return userNickname + "님 | " + createdAt.toLocalDate() + "\n"
                + (status == TaskStatus.COMPLETED ? "[ v ]  " : "[   ]  ")
                + taskContents;
    }
}
