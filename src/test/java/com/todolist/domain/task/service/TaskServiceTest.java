//package com.todolist.domain.task.service;
//
//import com.todolist.domain.category.model.Category;
//import com.todolist.domain.category.service.CategoryService;
//import com.todolist.domain.task.dao.TaskDao;
//import com.todolist.domain.task.model.Task;
//import com.todolist.domain.task.model.TaskStatus;
//import com.todolist.jdbc.config.JDBCConnection;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//class TaskServiceTest {
//    private static final Logger log = LoggerFactory.getLogger(TaskServiceTest.class);
//    private Connection connection;
//    private TaskService taskService;
//    private CategoryService categoryService;
//    private TaskDao taskDao;
//    private static final String TEST_TITLE = "test_title";
//    private static final String TEST_CONTENTS = "test_contents";
//    private static final TaskStatus TEST_STATUS = TaskStatus.IN_PROGRESS;
//
//    /**
//     * ✅ 테스트 전에 DB 연결 및 트랜잭션 설정
//     */
//    @BeforeEach
//    void setUp() throws SQLException {
//        connection = JDBCConnection.getConnection();
//        connection.setAutoCommit(false); // 자동 커밋 방지 (테스트 종료 후 롤백)
//        taskService = new TaskService(connection);
//        categoryService = new CategoryService(connection);
//
//        // Arrange : 테스트용 사용자 추가
//        categoryService.addCategory(new Category(0, TEST_TITLE,null));
//        taskService.addTask(new Task(0, 0, 0, TEST_CONTENTS, null, null, null, TEST_STATUS, 0));
//
//        log.info("테스트용 사용자 추가 완료");
//    }
//
//    /**
//     * ✅ 카테고리별 Task 조회 테스트
////     */
////    @Test
////    @DisplayName("✅ 카테고리별 Task 조회 성공")
////    void testGetTaskByCategorySuccess() throws SQLException {
////        // When (서비스 메서드 실행)
////        List<TodoAndCategoryDto> result = taskService.getTaskByCategory(0);
////
////        // Then (검증)
////        assertNotNull(result);
////        assertEquals(2, result.size());
////
////        assertEquals(0, result.get(0).getTaskId());
////        assertEquals(TEST_TITLE, result.get(0).getCategoryTitle());
////        assertEquals(TEST_CONTENTS, result.get(0).getTaskContents());
////
////        log.info("✅ 카테고리별 Task 조회 성공 테스트 통과");
////    }
//
//    /**
//     * ✅ 테스트 후 DB 롤백하여 데이터 원상복구
//     */
//    @AfterEach
//    void tearDown() {
//        try {
//            connection.rollback(); // 트랜잭션 롤백
//            connection.setAutoCommit(true);
//            connection.close();
//            log.info("테스트 완료 후 데이터 롤백");
//        } catch (SQLException e) {
//            log.error("테스트 후 롤백 중 오류 발생", e);
//        }
//    }
//
//}