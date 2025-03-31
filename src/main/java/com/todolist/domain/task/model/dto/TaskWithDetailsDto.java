package com.todolist.domain.task.model.dto;

import com.todolist.domain.task.model.TaskStatus;

import java.time.LocalDateTime;

public class TaskWithDetailsDto {
    private int taskId;
    private String categoryTitle;
    private String userNickname;
    private String contents;
    private LocalDateTime createdAt;
    private TaskStatus status;

    public TaskWithDetailsDto(int taskId, String categoryTitle, String userNickname, String contents, LocalDateTime createdAt, TaskStatus status) {
        this.taskId = taskId;
        this.categoryTitle = categoryTitle;
        this.userNickname = userNickname;
        this.contents = contents;
        this.createdAt = createdAt;
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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
        return "[" + categoryTitle + "]  " + userNickname + "님 | " + createdAt.toLocalDate() + "\n   "
                + (status == TaskStatus.COMPLETED ? "[ v ]  " : "[   ]  ")
                + contents;
    }
}
