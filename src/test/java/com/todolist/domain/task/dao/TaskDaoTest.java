package com.todolist.domain.task.dao;

import com.todolist.domain.category.dao.CategoryDao;
import com.todolist.domain.category.model.Category;
import com.todolist.domain.task.model.Task;
import com.todolist.domain.task.model.TaskStatus;
import com.todolist.domain.task.model.dto.TaskWithDetailsDto;
import com.todolist.domain.task.model.dto.TodoAndCategoryDto;
import com.todolist.domain.user.dao.UserDao;
import com.todolist.domain.user.model.User;
import com.todolist.jdbc.config.JDBCConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskDaoTest {
    private Connection connection;
    private TaskDao taskDao;
    private UserDao userDao;
    private CategoryDao categoryDao;

    // Task 상수 정의
    private static final String TEST_CONTENTS = "test_contents";
    private static final TaskStatus TEST_STATUS = TaskStatus.IN_PROGRESS;
    // User 상수 정의
    private static final String TEST_NICKNAME = "nickname";
    private static final String TEST_EMAIL = "test_email";
    private static final String TEST_PASSWORD = "test_password";
    // Category 상수 정의
    private static final String TEST_TITLE = "test_title";

    private int testTaskId;
    private int testUserId;
    private int testCategoryId;

    /**
     * ✅ 테스트 전 DB 연결 및 트랜잭션 설정
     */
    @BeforeEach
    void setUp() {
        try {
            connection = JDBCConnection.getConnection();
            connection.setAutoCommit(false); // 테스트 후 롤백을 위해 AutoCommit 비활성화
            taskDao = new TaskDao(connection);
            userDao = new UserDao(connection);
            categoryDao = new CategoryDao(connection);

            // Arrange: 테스트 데이터 삽입
            List<User> users = userDao.getAllUsers();
            testUserId = users.get(users.size() - 1).getUserId();
            User testUser = new User(0, TEST_NICKNAME, TEST_EMAIL, TEST_PASSWORD, null, null, null,0);
            userDao.registerUser(testUser);

            List<Category> categories = categoryDao.getAllCategories();
            testCategoryId = categories.get(categories.size() - 1).getCategoryId();
            Category testCategory = new Category(0, TEST_TITLE, null);
            categoryDao.addCategory(testCategory);

            Task testTaskContents = new Task(0, testUserId, testCategoryId, TEST_CONTENTS,
                    null, null, null, TEST_STATUS, 0);
            taskDao.addTaskContents(testTaskContents);

            List<TaskWithDetailsDto> tasks = taskDao.getAllTasks();
            testTaskId = tasks.get(tasks.size() - 1).getTaskId();

        } catch (SQLException e) {
            throw new RuntimeException("테스트 데이터 준비 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * To Do List 전체 조회 테스트 (READ)
     */
    @Test
    @DisplayName("To Do List 전체 조회 테스트")
    void testGetAllTasks() {
        // Act: DAO를 통해 데이터 조회
        List<TaskWithDetailsDto> tasks = taskDao.getAllTasks();

        // Assert: 결과 검증
        Assertions.assertNotNull(tasks, "To Do List 목록은 null이 아니어야 합니다.");
        Assertions.assertFalse(tasks.isEmpty(), "To Do List 목록은 비어 있지 않아야 합니다.");
    }

    /**
     * To Do List 카테고리 별 조회 (READ - category_id)
     */
    @Test
    @DisplayName("To Do List 카테고리 별 조회 테스트")
    void testGetTaskByCategory() {
        // Act: 특정 TASK 조회
        List<TodoAndCategoryDto> retrievedTask = taskDao.getTasksByCategory(testCategoryId);

        // Assert: 조회된 값 검증
        Assertions.assertNotNull(retrievedTask, "To Do List가 존재해야 합니다.");
        Assertions.assertEquals(TEST_CONTENTS, retrievedTask.get(0).getTaskContents(), "내용이 일치해야 합니다.");
    }

    /**
     * To Do List 내용 등록 (CREATE)
     */
    @Test
    @DisplayName("To Do List 내용 등록")
    void testAddTaskContents() {
        // Arrange: 새로운 사용자 객체 생성
        Task newTask = new Task(0, testUserId, testCategoryId, TEST_CONTENTS, null, null, null, TEST_STATUS, 0);

        // Act: 사용자 추가
        boolean isAdded = taskDao.addTaskContents(newTask);

        // Assert: 추가 확인
        Assertions.assertTrue(isAdded, "To Do List가 성공적으로 추가되어야 합니다.");
    }

    /**
     * To Do List 상태 변경 (UPDATE)
     */
    @Test
    @DisplayName("To Do List 상태 변경 테스트")
    void testUpdateTaskStatus() {
        // Arrange: 기존 To Do List 상태 정보 변경
        Task updateTaskStatus = new Task(testTaskId, testUserId, testCategoryId, TEST_CONTENTS, null, null, null, TEST_STATUS, 0);

        // Act: To Do List 상태 정보 업데이트
        boolean isUpdated = taskDao.updateTaskStatus(updateTaskStatus);

        // Assert: 업데이트 결과 확인
        Assertions.assertTrue(isUpdated, "To Do List 상태 정보가 성공적으로 수정되어야 합니다.");

        // 업데이트된 데이터 조회 후 검증
        List<TodoAndCategoryDto> retrievedTask = taskDao.getTasksByCategory(testCategoryId);
        Assertions.assertEquals(TaskStatus.COMPLETED, retrievedTask.get(0).getStatus(), "status가 \"COMPLETED\"로 변경되어야 합니다.");
    }

    /**
     * To Do List 삭제 (UPDATE - soft delete)
     */
    @Test
    @DisplayName("To Do List 삭제 테스트")
    void testSoftDeleteTask() throws SQLException {
        // Arrange: 기존 사용자 정보 변경
        Task softDeleteTask = new Task(0, testUserId, testCategoryId, TEST_CONTENTS,
                null, null, null, TEST_STATUS, 0);

        // Act: 사용자 정보 업데이트
        boolean isUpdated = taskDao.softDeleteTask(testUserId, 0);

        // Assert: 업데이트 결과 확인
        Assertions.assertFalse(isUpdated, "To Do List 삭제 정보가 성공적으로 수정되어야 합니다.");
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