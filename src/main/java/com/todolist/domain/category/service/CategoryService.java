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

    /**
     * 📌 모든 카테고리 조회 (READ)
     * - 데이터 검증 후 반환
     */
    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = categoryDao.getAllCategories();

        if(categories == null) {
            log.error("조회한 카테고리의 정보가 없거나 DB와 연결하는 과정에서 오류가 발생했습니다.");
            return null;
        }

        return categoryDao.getAllCategories();
    }

    /**
     * 📌 단일 카테고리 조회 (READ)
     * - 이 메서드는 주어진 카테고리 title 을 기반으로 데이터베이스에서 카테고리를 조회합니다.
     * @param title 조회할 카테고리의 제목
     * @return 조회된 'Category' 객체를 반환. 카테고리가 존재하지 않을 경우, 예외 발생
     * @throws IllegalArgumentException 해당 title 의 카테고리가 존재하지 않을 경우 발생
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */
    public Category getCategoryByTitle(String title) throws SQLException {
        Category category = categoryDao.getCategoryByTitle(title);

        if (category == null) {
            throw new IllegalArgumentException("❌ 해당 제목의 카테고리를 찾을 수 없습니다.");
        }
        return category;
    }

    /**
     * 📌 카테고리 추가 (CREATE)
     * - 이 메서드는 카테고리 제목 중복 체크 후 카테고리를 추가합니다.
     * @param category 카테고리 객체를 전달받음
     * @return categoryDao 의 addCategory() 반환
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생합니다.
     * @throws IllegalArgumentException 중복하는 카테고리가 존재하는 경우 발생
     */
    public boolean addCategory(Category category) throws SQLException {
        List<Category> categories = categoryDao.getAllCategories();
        for (Category c : categories) {
            if (c.getTitle().equals(category.getTitle())) {
                throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
            }
        }
        return  categoryDao.addCategory(category);
    }
}
