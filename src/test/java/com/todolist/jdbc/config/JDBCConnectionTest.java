package com.todolist.jdbc.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

class JDBCConnectionTest {

    @BeforeAll
    static void setUp() {
        System.out.println("테스트 시작");
    }

    @Test
    void testConnection() throws SQLException {
        try (Connection con = JDBCConnection.getConnection()) {
            Assertions.assertNotNull(con, "connection이 생성되지 않았습니다.");
            Assertions.assertFalse(con.isClosed(), "커넥션은 열려있어야 합니다.");
            System.out.println("DB 커넥션이 연결되었습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() {
        System.out.println("테스트 종료 및 자원 반납");
        JDBCConnection.close();
    }
}