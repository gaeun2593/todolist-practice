package com.todolist.domain.category.service;

import com.todolist.domain.category.dao.CategoryDao;
import com.todolist.domain.category.model.Category;
import com.todolist.jdbc.config.JDBCConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceTest.class);
    private Connection connection;
    private CategoryService categoryService;
    private CategoryDao categoryDao;
    private static final String TEST_TITLE = "test_title";

    /**
     * ✅ 테스트 전에 DB 연결 및 트랜잭션 설정
     */
    @BeforeEach
    void setUp() throws SQLException {
        connection = JDBCConnection.getConnection();
        connection.setAutoCommit(false); // 자동 커밋 방지 (테스트 종료 후 롤백)
        categoryService = new CategoryService(connection);

        // Arrange : 테스트용 사용자 추가
        categoryService.addCategory(new Category(0, TEST_TITLE,null));

        log.info("테스트용 사용자 추가 완료");
    }

    /**
     * ✅ 전체 카테고리 조회 테스트
     */
    @Test
    @DisplayName("전체 카테고리 조회 테스트")
    void getAllCategories() throws SQLException {
        // Act
        List<Category> categories = categoryService.getAllCategories();

        // Assert
        assertFalse(categories.isEmpty(), "카테고리 목록이 비어 있으면 안됩니다.");

        // 컬렉션과 같은 데이터 구조에서 데이터를 쉽게 처리하기 위한 용도의 API
        assertTrue(categories.stream()
                        // anyMatch 주어진 조건을 만족하는 요소가 스트림에 하나라도 존재하는지를 확인하는 데 사용
                        .anyMatch(user -> user.getTitle().equals(TEST_TITLE)),
                "테스트 카테고리가 존재해야 합니다.");
    }

    /**
     * ✅ 단일 카테고리 조회 (타이틀) 테스트
     */
    @Test
    @DisplayName("단일 카테고리 조회 (타이틀) 테스트")
    void testGetCategoryByTitle() throws SQLException {
        // Arrange
        List<Category> categories = categoryService.getAllCategories();

        Optional<String> title = categories.stream()
                // 같은 값을 가진 데이터만 필터링 한다.
                .filter(category -> category.getTitle().equals(TEST_TITLE))
                // 사용자의 id를 추출한다.
                .map(Category::getTitle)
                // 조건을 만족하는 첫 번째 요소를 반환한다.
                .findFirst();


        // Act
        Category category = categoryService.getCategoryByTitle(title.get());

        // Assert
        assertNotNull(category, "카테고리가 존재해야 합니다.");
        assertEquals(TEST_TITLE, category.getTitle(), "카테고리가 일치해야 합니다.");
    }

    /**
     * ✅ 테스트 후 DB 롤백하여 데이터 원상복구
     */
    @AfterEach
    void tearDown() {
        try {
            connection.rollback(); // 트랜잭션 롤백
            connection.setAutoCommit(true);
            connection.close();
            log.info("테스트 완료 후 데이터 롤백");
        } catch (SQLException e) {
            log.error("테스트 후 롤백 중 오류 발생", e);
        }
    }
}