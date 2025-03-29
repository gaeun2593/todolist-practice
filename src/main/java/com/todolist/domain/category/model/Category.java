package com.todolist.domain.category.model;

import java.time.LocalDateTime;

public class Category {

    private int categoryId;
    private String title;
    private LocalDateTime createdAt;

    public Category(int categoryId, String title, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.title = title;
        this.createdAt = createdAt;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
