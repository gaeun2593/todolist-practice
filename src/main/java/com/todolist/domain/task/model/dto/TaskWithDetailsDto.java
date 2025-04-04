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

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "[" + categoryTitle + "]  " + userNickname + "님 | " + createdAt.toLocalDate() + "\n   "
                + (status == TaskStatus.COMPLETED ? "[ v ]  " : "[   ]  ")
                + contents;
    }
}
