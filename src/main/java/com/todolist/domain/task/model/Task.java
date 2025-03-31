package com.todolist.domain.task.model;

import com.todolist.domain.category.model.Category;

import java.time.LocalDateTime;

public class Task {

    private int taskId;
    private int userId;
    private int categoryId;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private TaskStatus status;
    private int isDeleted;

    public Task(int taskId, int userId, int categoryId, String contents, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, TaskStatus complete, int isDeleted) {
        this.taskId = taskId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.status = complete;
        this.isDeleted = isDeleted;
    }

    public Task() {}
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("[ ").append(categoryId).append(" ] ")
                .append(createdAt.toLocalDate()).append(" | ")
                .append("작성자 ID: ").append(userId).append("\n")
                .append(contents).append("        ");

        result.append(status == TaskStatus.COMPLETED ? "[ v ]" : "[   ]");

        return result.toString();
    }
}
