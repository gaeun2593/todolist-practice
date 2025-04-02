package com.todolist;

import com.todolist.domain.task.view.TaskView;
import com.todolist.domain.user.model.User;
import com.todolist.domain.user.view.UserView;

import java.sql.*;
import java.util.Scanner;

public class MainView {

    private Scanner scanner;
    private Connection connection;

    public MainView(Connection connection, Scanner scanner) {
        this.scanner = scanner;
        this.connection = connection;
    }

    public void showMainMenu(User loggedInUser) throws SQLException {
        while (true) {
            System.out.println("\n❣️ 메인 화면 ❣️");
            System.out.println("1. 투두리스트 관리 - 조회, 검색, 등록");
            System.out.println("2. 회원 관리 - 닉네임 변경, 회원 탈퇴");
            System.out.println("0. 로그아웃");
            System.out.print("\n선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1 -> {
                    TaskView taskView = new TaskView(connection);
                    taskView.mainTask(connection, loggedInUser);
                }
                case 2 -> {
                    UserView userView = new UserView(connection);
                    userView.userMenu(loggedInUser);
                }
                case 0 -> {
                    System.out.println("🖐️ " + loggedInUser.getNickname() + "님, 다음에 봬요!");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }
}
