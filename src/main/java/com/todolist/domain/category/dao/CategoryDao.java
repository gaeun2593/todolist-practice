package com.todolist.domain.category.dao;

import com.todolist.domain.category.model.Category;
import com.todolist.util.QueryUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private final Connection connection;

    public CategoryDao(Connection connection) { this.connection = connection; }

    /**
     * 📌 카테고리 전체 조회 (READ)
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = QueryUtil.getQuery("getAllCategories"); // XML에서 쿼리 로드

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * 📌 카테고리 단일 조회 (READ)
     * - 카테고리 제목 조회
     */
    public Category getCategoryByTitle(String title) {
        String query = QueryUtil.getQuery("getCategoryByTitle");
        Category category = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    /**
     * 📌 카테고리 등록 (CREATE)
     */
    public boolean addCategory(Category category) {
        String query = QueryUtil.getQuery("addCategory");

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getTitle());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
