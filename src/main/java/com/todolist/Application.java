package com.todolist;

import com.todolist.domain.user.model.User;
import com.todolist.domain.user.view.UserView;
import com.todolist.jdbc.config.JDBCConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws SQLException {
        Connection connection = JDBCConnection.getConnection();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n📔 TO DO LIST 📔");
            System.out.println("1. 회원 가입");
            System.out.println("2. 로그인");
            System.out.println("0. 종료");
            System.out.print("\n선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> startRegisterUser(connection, scanner);
                case 2 -> startLogIn(connection, scanner);
                case 0 -> {
                    connection.close();
                    System.out.println("🚀 프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("❌ 잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    // 1️⃣ 회원가입
    private static void startRegisterUser(Connection connection, Scanner scanner) {
        UserView userView = new UserView(connection);
        userView.registerUser();
    }

    // 2️⃣ 로그인
    private static void startLogIn(Connection connection, Scanner scanner) throws SQLException {
        UserView userView = new UserView(connection);
        User loggedInUser = userView.logInUser();

        if (loggedInUser != null) {
            MainView mainView = new MainView(connection, scanner);
            mainView.showMainMenu(loggedInUser);
        } else {
            System.out.println("❌ 로그인 실패! 다시 시도하세요.");
        }
    }
}
