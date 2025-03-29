package com.todolist.jdbc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


// config : JDBC 설정 정보
public class JDBCConnection {

    private static final HikariDataSource dataSource;

    static {
        try {

            Properties props = new Properties();

            props.load(JDBCConnection.class.getClassLoader().getResourceAsStream("config.properties"));
            HikariConfig config = new HikariConfig();

            // DB 접속을 위한 설정 정보
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.username"));
            config.setPassword(props.getProperty("db.password"));

            // 최대 커넥션
            config.setMaximumPoolSize(10);

            // 최소 커넥션
            config.setMinimumIdle(5);

            // 유휴 상태면 커넥션 닫기 즉, 30초 동안 아무런 요청이 없으면 커넥션을 닫는다
            config.setIdleTimeout(30000);

            config.setMaxLifetime(1800000); // 30분 후 커넥션을 새로 생성한다.

            config.setConnectionTimeout(2000);// 최대 2초 대기 후 타임 아웃

            dataSource = new HikariDataSource(config);

        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // 커넥션 풀에서 연결되어 있는 객체를 꺼내오는 메서드
    public static Connection getConnection() throws SQLException {

        // connection pool에서 connection 객체를 꺼내옴
        return dataSource.getConnection();
    }

    // hikaricp 전체 커넥션 풀을 종료하는 메서드
    // application -> 전체 종료 -> connection pool 더 이상 사용 불가.
    public static void close(){
        if(dataSource != null){
            dataSource.close();
        }
    }

    public static void printConnectionPoolStatus() {
        HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();
        System.out.println("[hikariCp 커넥션 풀 상태 ]");
        System.out.println("총 커넥션 수 (total Connections) : " + poolMXBean.getTotalConnections());
        System.out.println("활성 커넥션 수 (Active Connections) : " + poolMXBean.getActiveConnections());
        System.out.println("유휴 커넥션 수 (IDLE Connections) : " + poolMXBean.getIdleConnections());
        System.out.println("대기 중인 커넥션 요청 수 (Pending Threads) : " + poolMXBean.getThreadsAwaitingConnection());
    }
}
