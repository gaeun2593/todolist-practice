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

    public Category(String title) {
        this.title = title;
        this.createdAt = LocalDateTime.now();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
