package com.todolist.domain.user.service;

import com.todolist.domain.user.dao.UserDao;
import com.todolist.domain.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDao userDao;
    private final Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
        this.userDao = new UserDao(connection);
    }

    /**
     * 📌 모든 유저 조회 (READ)
     * - 데이터 검증 후 반환
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> users = userDao.getAllUsers();

        if(users == null) {
            log.error("조회한 사용자의 정보가 없거나 DB와 연결하는 과정에서 오류가 발생했습니다.");
            return null;
        }

        return userDao.getAllUsers();
    }

    /**
     * 📌 단일 유저 조회 (READ)
     * - 주어진 유저의 email 을 기반으로 데이터베이스에서 사용자를 조회
     * @param email 조회할 유저의 이메일
     * @return 조회된 'User' 객체를 반환. 유저가 존재하지 않을 경우, 예외 발생
     * @throws IllegalArgumentException 해당 email 의 유저가 존재하지 않는 경우 발생
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */
    public User getUserByEmail(String email) throws SQLException {
        User user = userDao.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("❌ 해당 이메일의 사용자를 찾을 수 없습니다.");
        }

        return user;
    }


    /**
     * 📌 유저 등록 (CREATE)
     * - 닉네임, 이메일 중복 체크 후 추가 (회원 가입)
     * @param user 사용자 객체를 전달받음
     * @return boolean 성공 여부를 boolean 타입으로 반환
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     * @throws IllegalArgumentException 중복되는 닉네임, 이메일이 존재하는 경우 발생
     */
    public boolean registerUser(User user) throws SQLException {
        List<User> existingUsers = getAllUsers();

        // 중복 닉네임 검사
        for (User u : existingUsers) {
            if (u.getNickname().equals(user.getNickname())) {
                throw new IllegalArgumentException("❌ 이미 존재하는 닉네임입니다.");
            }
        }

        // 중복 이메일 검사
        for (User u : existingUsers) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("❌ 이미 존재하는 이메일입니다.");
            }
        }

        return userDao.registerUser(user);
    }

    /**
     * 📌 이메일 정규식 검사
     * - 로그인 시 이메일 정규식을 검사하기 위해 사용
     */
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    /**
     * 📌 사용자 닉네임 변경 (UPDATE)
     * - 기존 사용자 존재 여부 확인 후 업데이트
     * - 닉네임 중복 여부 검증 추가
     * @param user 수정할 사용자 정보를 포함하는 User 객체
     * @return boolean 사용자 정보 수정 성공 여부를 반환합니다.
     * @throws IllegalArgumentException 수정할 사용자가 존재하지 않거나, 닉네임이 이미 존재하는 경우 발생합니다.
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생합니다.
     */
    public boolean updateNickname(User user) throws SQLException {
        // 기존 사용자 이메일 존재 여부 확인
        User existingUser = getUserByEmail(user.getEmail());

        if (existingUser == null) {
            throw new IllegalArgumentException("❌ 수정할 사용자를 찾을 수 없습니다.");
        }

        // 닉네임 존재하는지 확인
        List<User> existingUsers = getAllUsers();
        for (User u : existingUsers) {
            if (u.getNickname().equals(user.getNickname())) {
                throw new IllegalArgumentException("❌ 이미 존재하는 닉네임입니다.");
            }
        }

        boolean result = userDao.updateNickname(user);
        if (!result) {
            throw new SQLException("수정하는 과정에서 오류가 발생되었습니다.");
        }

        return result;
    }

    /**
     * 📌 회원 탈퇴 (soft delete - UPDATE)
     * - 이메일과 비밀번호 조회 후 탈퇴 정보(is_delete = 1) 업데이트
     * @param user 탈퇴할 사용자 정보를 포함하는 User 객체
     * @return boolean 사용자 정보 탈퇴 성공 여부를 반환
     * @throws IllegalArgumentException 탈퇴할 사용자가 존재하지 않거나, 비밀번호가 일치하지 않는 경우 발생
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */
    public boolean softDeleteUser(User user) throws SQLException {
        // 기존 사용자 이메일 존재 여부 확인
        User existingUser = getUserByEmail(user.getEmail());

        if (existingUser == null) {
            throw new IllegalArgumentException("❌ 탈퇴할 사용자를 찾을 수 없습니다.");
        }

        // 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호 비교
        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("❌ 비밀번호가 일치하지 않습니다.");
        }

        boolean result = userDao.softDeleteUser(user);

        if (!result) {
            throw new SQLException("탈퇴하는 과정에서 오류가 발생되었습니다.");
        }

        return result;
    }
}
