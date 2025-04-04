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

    public String getTaskContents() {
        return taskContents;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return userNickname + "님 | " + createdAt.toLocalDate() + "\n"
                + (status == TaskStatus.COMPLETED ? "[ v ]  " : "[   ]  ")
                + taskContents;
    }
}
