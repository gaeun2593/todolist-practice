package com.todolist.domain.user.dao;

import com.todolist.domain.user.model.User;
import com.todolist.util.QueryUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * 📌 회원가입 (CREATE)
     */
    public boolean registerUser(User user) {
        String query = QueryUtil.getQuery("registerUser");

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getNickname());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 📌 이메일 조회 (READ) - 로그인, 내 정보 확인 시 필요
     */
    public User getUserByEmail(String email) {
        String query = QueryUtil.getQuery("getUserByEmail");
        User user = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at") != null
                                ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getTimestamp("deleted_at") != null
                                ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("is_deleted")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 📌 전체 유저 조회 (READ)
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllUsers");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at") != null
                                ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                        rs.getTimestamp("deleted_at") != null
                                ? rs.getTimestamp("deleted_at").toLocalDateTime() : null,
                        rs.getInt("is_deleted")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 📌 닉네임 변경 (UPDATE)
     */
    public boolean updateNickname(User user) {
        String query = QueryUtil.getQuery("updateNickname");

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getNickname());
            ps.setString(2, user.getEmail());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 📌 회원 탈퇴 (UPDATE - soft delete 사용)
     */
    public boolean softDeleteUser(User user) {
        String query = QueryUtil.getQuery("softDeleteUser");

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
