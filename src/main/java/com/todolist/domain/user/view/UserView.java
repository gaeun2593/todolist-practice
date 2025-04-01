package com.todolist.domain.user.view;

import com.todolist.domain.user.model.User;
import com.todolist.domain.user.service.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class UserView {

    private final UserService userService;
    private final Scanner scanner;

    public UserView(Connection connection) {
        this.userService = new UserService(connection);
        this.scanner = new Scanner(System.in);
    }

    // 사용자 관리 화면
    public void userMenu(User loggedInUser) {
        while (true) {
            System.out.println("\n👥 사용자 관리 👥");
            System.out.println("1. 닉네임 변경");
            System.out.println("2. 회원 탈퇴");
            System.out.println("3. 내 정보");
            System.out.println("0. 메인 화면으로");
            System.out.print("\n선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1 -> updateNickname(loggedInUser);
                case 2 -> softDeleteUser(loggedInUser);
                case 3 -> showUserInfo(loggedInUser);
                case 0 -> {
                    return; // 메인 화면으로 돌아가기
                }
                default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    // 1️⃣ 회원 등록 (CREATE)
    public void registerUser() {
        String nickname;

        // 닉네임 길이 제한 (10자 이하)
        while (true) {
            System.out.print("✏️ 닉네임을 입력해주세요 (최대 10자): ");
            nickname = scanner.nextLine();

            if (nickname.length() <= 10) {
                break;
            } else {
                System.out.println("❌ 닉네임은 최대 10자까지 입력할 수 있습니다. 다시 입력해주세요.");
            }
        }

        String email;
        while (true) {
            System.out.print("✏️ 이메일을 입력해주세요: ");
            email = scanner.nextLine();

            if (userService.isValidEmail(email)) {
                break;
            } else {
                System.out.println("❌ 유효하지 않은 이메일 형식입니다. 다시 입력해주세요.");
            }
        }

        System.out.print("✏️ 비밀번호를 입력해주세요 : ");
        String password = scanner.nextLine();

        User user = new User(0, nickname, email, password, null, null, null,0);
        try {
            boolean success = userService.registerUser(user);
            if (success) {
                System.out.println("✅ 회원 등록이 완료되었습니다.");
            } else {
                System.out.println("❌ 회원 등록에 실패하였습니다.");
            }
        } catch (SQLException e) {
            System.out.println("❌ 회원 등록 중 오류가 발생했습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // 2️⃣ 로그인 (READ)
    public User logInUser() {
        String email;
        String password;

        try {
            while (true) {
                System.out.print("✏️ 이메일을 입력하세요: ");
                email = scanner.nextLine();

                User existingUser = userService.getUserByEmail(email);

                if (existingUser == null) {
                    System.out.println("❌ 해당 이메일의 사용자를 찾을 수 없습니다.");
                    continue;
                }

                System.out.print("✏️ 비밀번호를 입력하세요: ");
                password = scanner.nextLine();

                if (!existingUser.getPassword().equals(password)) {
                    System.out.println("❌ 비밀번호가 일치하지 않습니다.");
                    continue;
                } else {
                    System.out.println("✅ " + existingUser.getNickname() + "님, 로그인에 성공하였습니다.");
                    return existingUser;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ 로그인 중 오류가 발생했습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // 3️⃣ 닉네임 변경 (UPDATE)
    public void updateNickname(User loggedInUser) {
        System.out.print("✏️ 새로운 사용자 닉네임을 입력하세요: ");
        System.out.println("0. 뒤로가기");
        System.out.print("\n입력: ");
        String newNickname = scanner.nextLine();

        if (Integer.parseInt(newNickname) == 0) {
            return;
        }

        try {
            loggedInUser.setNickname(newNickname);
            boolean success = userService.updateNickname(loggedInUser);

            if (success) {
                System.out.println("✅ 사용자 닉네임이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("❌ 사용자 닉네임 수정에 실패하였습니다.");
            }
        } catch (SQLException e) {
            System.out.println("❌ 사용자 닉네임 수정 중 오류가 발생했습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // 4️⃣ 회원 탈퇴 (soft delete)
    public void softDeleteUser(User loggedInUser) {
        System.out.println("✏️ 탈퇴할 사용자의 비밀번호를 입력하세요");
        System.out.println("0. 뒤로가기");
        System.out.print("\n입력: ");
        String password = scanner.nextLine();

        if (Integer.parseInt(password) == 0) {
            return;
        }

        try {
            // 입력한 비밀번호와 저장된 비밀번호 비교
            if (!loggedInUser.getPassword().equals(password)) {
                System.out.println("❌ 비밀번호가 일치하지 않습니다.");
                return;
            }

            // 비밀번호가 일치하면 소프트 딜리트 실행
            boolean success = userService.softDeleteUser(loggedInUser);

            if (success) {
                System.out.println("✅ 회원 탈퇴가 완료되었습니다.");
            } else {
                System.out.println("❌ 회원 탈퇴에 실패하였습니다.");
            }
        } catch (SQLException e) {
            System.out.println("❌ 회원 탈퇴 중 오류가 발생했습니다.");
        }
    }

    // 회원 정보
    public void showUserInfo(User loggedInUser) {
        while(true) {
            System.out.println("📍 내 정보 📍");
            System.out.println("이메일 : " + loggedInUser.getEmail());
            System.out.println("닉네임 : " + loggedInUser.getNickname());
            System.out.println("가입일 : " + loggedInUser.getCreatedAt().toLocalDate());
            System.out.println("0. 뒤로가기");
            System.out.print("\n선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            if (choice == 0) {
                return;
            }
        }
    }
}
