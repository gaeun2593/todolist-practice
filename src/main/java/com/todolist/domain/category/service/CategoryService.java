package com.todolist.domain.category.service;

import com.todolist.domain.category.dao.CategoryDao;
import com.todolist.domain.category.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryDao categoryDao;
    private final Connection connection;

    public CategoryService(Connection connection) {
        this.connection = connection;
        this.categoryDao = new CategoryDao(connection);
    }

    // 전체 카테고리 조회
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = categoryDao.getAllCategories();

        if(categories == null) {
            log.error("조회한 카테고리의 정보가 없거나 DB와 연결하는 과정에서 오류가 발생했습니다.");
            return null;
        }

        return categoryDao.getAllCategories();
    }

    // 단일 카테고리 조회 (타이틀)
    public Category getCategoryByTitle(String title) throws SQLException {
        Category category = categoryDao.getCategoryByTitle(title);

        if (category == null) {
            throw new IllegalArgumentException("❌ 해당 제목의 카테고리를 찾을 수 없습니다.");
        }
        return category;
    }

    // 카테고리 추가
    public boolean addCategory(Category category) throws SQLException {
        return  categoryDao.addCategory(category);
    }
}
