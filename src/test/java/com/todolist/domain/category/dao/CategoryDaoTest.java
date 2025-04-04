package com.todolist.domain.category.dao;

import com.todolist.domain.category.model.Category;
import com.todolist.jdbc.config.JDBCConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryDaoTest {
    private Connection connection;
    private CategoryDao categoryDao;
    private static final String TEST_TITLE = "test_title";
    private int testCategoryId;


    /**
     * ✅ 테스트 전 DB 연결 및 트랜잭션 설정
     */
    @BeforeEach
    void setUp() {
        try {
            connection = JDBCConnection.getConnection();
            connection.setAutoCommit(false); // 테스트 후 롤백을 위해 AutoCommit 비활성화
            categoryDao = new CategoryDao(connection);

            // Arrange: 테스트 데이터 삽입
            Category testCategory = new Category(0, TEST_TITLE, null);
            categoryDao.addCategory(testCategory);

            // 삽입된 데이터 확인 (테스트용 ID 저장)
            List<Category> categories = categoryDao.getAllCategories();
            testCategoryId = categories.get(categories.size() - 1).getCategoryId();

        } catch (SQLException e) {
            throw new RuntimeException("테스트 데이터 준비 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 전체 카테고리 조회 테스트 (READ)
     */
    @Test
    @DisplayName("전체 카테고리 조회 테스트")
    void testGetAllCategories() {
        // Act: DAO를 통해 데이터 조회
        List<Category> categories = categoryDao.getAllCategories();

        // Assert: 결과 검증
        Assertions.assertNotNull(categories, "카테고리 목록은 null이 아니어야 합니다.");
        Assertions.assertFalse(categories.isEmpty(), "카테고리 목록은 비어 있지 않아야 합니다.");
    }

    /**
     * 단일 카테고리 조회 테스트 (READ)
     */
    @Test
    @DisplayName("단일 카테고리 조회 테스트")
    void testGetgetCategoryByTitle() {
        // Act: 특정 카테고리 조회
        Category retrievedCategory = categoryDao.getCategoryByTitle(TEST_TITLE);

        // Assert: 조회된 값 검증
        Assertions.assertNotNull(retrievedCategory, "카테고리가 존재해야 합니다.");
        Assertions.assertEquals(TEST_TITLE, retrievedCategory.getTitle(), "카테고리 이름이 일치해야 합니다.");
    }

    /**
     * 카테고리 등록 테스트 (CREATE)
     */
    @Test
    @DisplayName("카테고리 등록 테스트")
    void testAddCategory() {
        // Arrange: 새로운 사용자 객체 생성
        Category newCategory = new Category(0, "new", null);

        // Act: 사용자 추가
        boolean isAdded = categoryDao.addCategory(newCategory);

        // Assert: 추가 확인
        Assertions.assertTrue(isAdded, "카테고리가 성공적으로 추가되어야 합니다.");
    }

    /**
     * ✅ 테스트 완료 후 롤백 (데이터 원상 복구)
     */
    @AfterEach
    void tearDown() {
        try {
            connection.rollback(); // 테스트 데이터 롤백
            connection.setAutoCommit(true);
            connection.close();
            System.out.println("테스트 완료: 데이터 롤백 성공");
        } catch (SQLException e) {
            System.err.println("테스트 종료 중 오류 발생: " + e.getMessage());
        }
    }
}