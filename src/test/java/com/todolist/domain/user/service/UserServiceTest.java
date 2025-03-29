package com.todolist.domain.user.service;

import com.todolist.domain.user.model.User;
import com.todolist.jdbc.config.JDBCConnection;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceTest.class);
    private Connection connection;
    private UserService userService;
    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_PASSWORD = "testpassword";
    private static final String TEST_NICKNAME = "TestUser";

    /**
     * ✅ 테스트 전에 DB 연결 및 트랜잭션 설정
     */
    @BeforeEach
    void setUp() throws SQLException {
        connection = JDBCConnection.getConnection();
        connection.setAutoCommit(false); // 자동 커밋 방지 (테스트 종료 후 롤백)
        userService = new UserService(connection);

        // Arrange : 테스트용 사용자 추가
        userService.registerUser(new User(0, TEST_NICKNAME, TEST_EMAIL, TEST_PASSWORD, null, null, null,0));

        log.info("테스트용 사용자 추가 완료");
    }

    /**
     * ✅ 모든 사용자 조회 테스트
     */
    @Test
    @DisplayName("모든 사용자 조회 테스트")
    void testGetAllUsers() throws SQLException {
        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertFalse(users.isEmpty(), "사용자 목록이 비어 있으면 안됩니다.");

        // 컬렉션과 같은 데이터 구조에서 데이터를 쉽게 처리하기 위한 용도의 API
        assertTrue(users.stream()
                        // anyMatch 주어진 조건을 만족하는 요소가 스트림에 하나라도 존재하는지를 확인하는 데 사용
                        .anyMatch(user -> user.getEmail().equals(TEST_EMAIL)),
                "테스트 계정이 존재해야 합니다.");
    }

    /**
     * ✅ 이메일 조회 테스트
     */
    @Test
    @DisplayName("이메일 조회 테스트")
    void testGetUserByEmail() throws SQLException {
        // Arrange
        List<User> users = userService.getAllUsers();

        Optional<String> email = users.stream()
                // 같은 값을 가진 데이터만 필터링 한다.
                .filter(user -> user.getEmail().equals(TEST_EMAIL))
                // 사용자의 id를 추출한다.
                .map(User::getEmail)
                // 조건을 만족하는 첫 번째 요소를 반환한다.
                .findFirst();


        // Act
        User user = userService.getUserByEmail(email.get());

        // Assert
        assertNotNull(user, "사용자가 존재해야 합니다.");
        assertEquals(TEST_EMAIL, user.getEmail(), "이메일이 일치해야 합니다.");
    }

    /**
     * ✅ 로그인 테스트
     */
    @Test
    @DisplayName("로그인 테스트")
    void testLoginUser() throws SQLException {
        // Arrange
        List<User> users = userService.getAllUsers();

        // 이메일과 비밀번호가 일치하는 사용자 필터링
        Optional<User> matchingUser = users.stream()
                .filter(u -> u.getEmail().equals(TEST_EMAIL) && u.getPassword().equals(TEST_PASSWORD))
                .findFirst();

        // 사용자가 일치하는지 확인
        assertTrue(matchingUser.isPresent(), "일치하는 사용자가 있어야 합니다.");

        // Act
        User user = matchingUser.get();
        User loggedInUser = userService.loginUser(user.getEmail(), user.getPassword());

        // Assert
        assertNotNull(user, "사용자가 존재해야 합니다.");
        assertEquals(TEST_EMAIL, user.getEmail(), "이메일이 일치해야 합니다.");
        assertEquals(TEST_PASSWORD, user.getPassword(), "비밀번호가 일치해야 합니다.");
    }

    /**
     * ✅ 중복 닉네임, 이메일 방지 테스트
     */
    @Test
    @DisplayName("중복 닉네임, 이메일 방지 테스트")
    void testDuplicateEmailRegistration() {
        // Arrange
        User duplicateUser = new User(0, TEST_NICKNAME, TEST_EMAIL, TEST_PASSWORD, null, null, null,0);

        // Act & Assert

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(duplicateUser),
                "중복된 닉네임, 이메일 등록 시 예외가 발생해야 합니다.");
    }

    /**
     * ✅ 닉네임 변경 테스트 (닉네임 중복 방지 포함)
     */
    @Test
    @DisplayName("닉네임 변경 테스트")
    void testUpdateUser() throws SQLException {
        // Arrange
        List<User> users = userService.getAllUsers();
        int userId = users.get(0).getUserId();
        User updatedNickname = new User(userId, "Updated", TEST_EMAIL, TEST_PASSWORD, null, null, null,0);

        // Act
        boolean isUpdated = userService.updateNickname(updatedNickname);
        User fetchedUser = userService.getUserByEmail(TEST_EMAIL);

        // Assert
        assertTrue(isUpdated, "사용자 정보 수정이 성공해야 합니다.");
        assertEquals("Updated", fetchedUser.getNickname(), "닉네임이 변경되어야 합니다.");
    }

    /**
     * ✅ 회원 탈퇴 테스트
     */
    @Test
    @DisplayName("회원 탈퇴 테스트")
    void testSoftDeleteUser() throws SQLException {
        // Arrange
        List<User> users = userService.getAllUsers();
        int userId = users.get(0).getUserId();
        User softDeleteUser = new User(userId, TEST_NICKNAME, TEST_EMAIL, TEST_PASSWORD, null, null, null,0);

        // Act
        boolean isDeleted = userService.softDeleteUser(softDeleteUser);
        User retrievedUser = userService.getUserByEmail(TEST_EMAIL);

        // Assert
        assertTrue(isDeleted, "회원 탈퇴가 성공해야 합니다.");
        Assertions.assertEquals(1, retrievedUser.getIsDeleted(), "is_deleted가 1로 변경되어야 합니다.");
        Assertions.assertNotNull(retrievedUser.getDeletedAt(), "deleted_at이 현재 시간으로 설정되어야 합니다.");    }

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