package com.todolist.domain.user.model;

import java.time.LocalDateTime;

public class User {

    private int userId;
    private String nickname;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private int isDeleted;

    public User(int userId, String nickname, String email, String password, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, int isDeleted) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.isDeleted = isDeleted;
    }

    public int getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    @Override
    public String toString() {
        return userId + ". " + "닉네임 : " + nickname + ", 이메일 : " + email + ", 등록일 : " + createdAt;
    }
}
