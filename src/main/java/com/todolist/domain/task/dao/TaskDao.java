package com.todolist.domain.task.dao;

import com.todolist.domain.task.model.TaskStatus;
import com.todolist.domain.task.model.Task;
import com.todolist.domain.task.model.dto.TaskWithDetailsDto;
import com.todolist.domain.task.model.dto.TodoAndCategoryDto;
import com.todolist.util.QueryUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    private final Connection connection;

    public TaskDao(Connection connection) {
        this.connection = connection;
    }

    // ✅ To Do List 전체 조회 (READ)
    public List<TaskWithDetailsDto> getAllTasks() {
        List<TaskWithDetailsDto> tasks = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllTasks"); // XML에서 쿼리 로드

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                TaskWithDetailsDto taskDto = new TaskWithDetailsDto(
                        rs.getInt("task_id"),
                        rs.getString("category_title"),
                        rs.getString("nickname"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        TaskStatus.valueOf(rs.getString("status"))
                );
                tasks.add(taskDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // To Do List 유저 별 조회 (READ)
    public List<TaskWithDetailsDto> getTasksByUser(int userId) throws SQLException {
        List<TaskWithDetailsDto> tasks = new ArrayList<>();
        String query = QueryUtil.getQuery("getTasksByUser");

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaskWithDetailsDto task = new TaskWithDetailsDto(
                        rs.getInt("task_id"),
                        rs.getString("category_title"),
                        rs.getString("nickname"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        TaskStatus.valueOf(rs.getString("status"))
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // ✅ To Do List 카테고리 별 조회 (READ)
    public List<TodoAndCategoryDto> getTasksByCategory(int categoryId) {
        List<TodoAndCategoryDto> tasks = new ArrayList<>();
        String query = QueryUtil.getQuery("getTaskByCategory");

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaskWithDetailsDto task = new TaskWithDetailsDto(
                        rs.getInt("task_id"),
                        rs.getString("category_title"),
                        rs.getString("nickname"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        TaskStatus.valueOf(rs.getString("status"))
                );
                tasks.add(task);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return tasks;
        }
    }

    // ✅ To Do List 내용 등록 (CREATE)
    public boolean addTaskContents(Task task) {
        String query = QueryUtil.getQuery("addTask");

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, task.getUserId());
            ps.setInt(2, task.getCategoryId());
            ps.setString(3, task.getContents());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ To Do List 상태 변경 -> COMPLETED (UPDATE)
    public boolean updateTaskStatus(Task task) {
        String query = QueryUtil.getQuery("updateTaskStatus");

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, task.getUserId());
            ps.setInt(2, task.getTaskId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ status가 IN_PROGRESS인 To Do List만 조회 (READ)
    public List<TaskWithDetailsDto> getTasksByUserInProgress(int userId) throws SQLException {
        List<TaskWithDetailsDto> tasks = new ArrayList<>();
        String query = QueryUtil.getQuery("getTasksByUserInProgress");

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaskWithDetailsDto task = new TaskWithDetailsDto(
                        rs.getInt("task_id"),
                        rs.getString("category_title"),
                        rs.getString("nickname"),
                        rs.getString("contents"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        TaskStatus.valueOf(rs.getString("status"))
                );
                tasks.add(task);
            }
            return tasks;
        }
    }

    // ✅ To Do List 삭제 (DELETE - Soft Delete)
    public boolean softDeleteTask (int userId, int taskId) throws SQLException {
        String deleteQuery = QueryUtil.getQuery("softDeleteTask");

        try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setInt(1, userId);
            ps.setInt(2, taskId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
