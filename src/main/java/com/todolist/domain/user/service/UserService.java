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

    // 전체 유저 조회
    public List<User> getAllUsers() throws SQLException {
        List<User> users = userDao.getAllUsers();

        if(users == null) {
            log.error("조회한 사용자의 정보가 없거나 DB와 연결하는 과정에서 오류가 발생했습니다.");
            return null;
        }

        return userDao.getAllUsers();
    }

    // 이메일 조회
    public User getUserByEmail(String email) throws SQLException {
        User user = userDao.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("❌ 해당 이메일의 사용자를 찾을 수 없습니다.");
        }
        return user;
    }


    // 회원가입 : 닉네임, 이메일 중복 체크 후 추가
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
        return userDao.addUser(user);
    }

    // 이메일 정규식 검사
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    // 로그인 : 이메일, 비밀번호 조회 후 테이블에 값이 있으면 성공
    public User loginUser(String email, String password) throws SQLException {
        User user = userDao.getUserByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("❌ 해당 이메일의 사용자를 찾을 수 없습니다.");
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("❌ 비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    // 닉네임 변경 : 이메일로 사용자 조회 후 닉네임 변경
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

    // 회원 탈퇴 : 이메일과 비밀번호 조회 후 탈퇴 정보(is_delete = 1) 업데이트
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
