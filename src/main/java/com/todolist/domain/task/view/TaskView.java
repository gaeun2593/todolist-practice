package com.todolist.domain.task.view;

import com.todolist.domain.task.model.dto.TaskWithDetailsDto;
import com.todolist.domain.task.service.TaskService;
import com.todolist.domain.user.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TaskView {

    private TaskService taskService;
    private static Scanner scanner;

    public TaskView(Connection connection) {
        this.taskService = new TaskService(connection);
        this.scanner = new Scanner(System.in);
    }

    public void mainTask(Connection connection, User loggedInUser) {
        TaskView taskView = new TaskView(connection);
        while (true) {
            System.out.println("\n📝 투두리스트 관리 📝");
            System.out.println("1. 전체 투두리스트 조회");
            System.out.println("2. 카테고리별 조회");
            System.out.println("3. 키워드 검색");
            System.out.println("4. 투두리스트 등록");
            System.out.println("5. 완료 표시하기");
            System.out.println("6. 투두리스트 삭제");
            System.out.println("0. 메인 화면으로");
            System.out.print("선택하세요: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 처리

            switch (choice) {
                case 1 -> taskView.getAllTasks(loggedInUser);
//                case 2 -> registerUser();
//                case 3 -> getUserById();
//                case 4 -> updateUser();
                case 5 -> updateTaskStatus(loggedInUser);
                case 6 -> softDeleteTask(loggedInUser);
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
            }
        }
    }

    // 1️⃣ 투두리스트 조회 (READ)
    private void getAllTasks(User loggedInUser) {
        try {
            List<TaskWithDetailsDto> tasks = taskService.getAllTasks();

            if (tasks.isEmpty()) {
                System.out.println("등록된 To Do List가 없습니다.");
            } else {
                while (true) {
                    System.out.println("\n📚 전체 투두리스트 목록 📚");
                    int count = 1;
                    for (TaskWithDetailsDto task : tasks) {
                        System.out.println(count + ". " + task);
                        count++;
                    }
                    System.out.println("\n1. 내 투두리스트 보기");
                    System.out.println("0. 메인 화면으로");
                    System.out.print("선택: ");
                    int input = scanner.nextInt();
                    scanner.nextLine();

                    switch (input) {
                        case 1 -> getTasksByUser(loggedInUser);
                        case 0 -> {
                            return;
                        }
                        default -> System.out.println("잘못된 입력입니다. 다시 선택하세요.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ To Do List를 조회하는 중 오류가 발생했습니다.");
        }
    }

    // 1️⃣ - 1. 유저 별 ToDoList 조회
    private void getTasksByUser(User loggedInUser) {
        try {
            List<TaskWithDetailsDto> tasks = taskService.getTasksByUser(loggedInUser);

            if (tasks.isEmpty()) {
                System.out.println("📌 현재 작성한 To Do List가 없습니다.");
            } else {
                System.out.println("\n📌 나의 TO DO LIST 📌");

                while (true) {
                    int count = 1;
                    for (TaskWithDetailsDto task : tasks) {
                        System.out.println(count + ". " + task);
                        count++;
                    }
                    System.out.println("\n0. 뒤로 가기");
                    System.out.print("선택: ");
                    int input = scanner.nextInt();
                    scanner.nextLine();

                    if (input == 0) {
                        return;
                    } else {
                        System.out.println("잘못된 입력입니다. 다시 선택하세요.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ To Do List를 조회하는 중 오류가 발생했습니다.");
        }
    }

    // 5️⃣ 완료 표시하기 (UPDATE)
    public void updateTaskStatus(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        try {
            List<TaskWithDetailsDto> tasks = taskService.getTasksByUserInProgress(loggedInUser);

            if (tasks.isEmpty()) {
                System.out.println("❌ 진행 중인 투두리스트가 없습니다.");
                return;
            }

            System.out.println("- 완료할 항목을 선택하세요 -");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i).getContents() + "   " + tasks.get(i).getCreatedAt().toLocalDate());
            }
            System.out.println("0. 뒤로가기");

            int choice;
            while (true) {
                System.out.print("\n선택: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice >= 0 && choice <= tasks.size()) {
                        break;
                    }
                } else {
                    scanner.next(); // 잘못된 입력 제거
                }
                System.out.println("❌ 올바른 숫자를 입력하세요.");
            }

            if (choice == 0) {
                return;
            }

            boolean result = taskService.completeTask(loggedInUser, tasks.get(choice - 1).getTaskId());

            if (result) {
                System.out.println("✅ 완료 상태로 변경되었습니다.");
            } else {
                System.out.println("❌ 변경이 실패되었습니다.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 6️⃣ 투두리스트 삭제 (soft delete - UPDATE)
    public void softDeleteTask(User loggedInUser) {
        try {
            List<TaskWithDetailsDto> tasks = taskService.getTasksByUser(loggedInUser);

            if (tasks.isEmpty()) {
                System.out.println("❌ 삭제할 투두리스트가 없습니다.");
                return;
            }

            System.out.println("- 삭제할 항목을 선택해주세요 -");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i).getContents());
            }
            System.out.println("0. 뒤로가기");

            int choice;
            while (true) {
                System.out.print("선택: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice >= 0 && choice <= tasks.size()) {
                        break;
                    }
                } else {
                    scanner.next();
                }
                System.out.println("❌ 올바른 숫자를 입력하세요.");
            }

            if (choice == 0) {
                return;
            }

            TaskWithDetailsDto selectedTask = tasks.get(choice - 1);

            System.out.println("⚠️ '" + selectedTask.getContents() + "' 을 삭제하시겠습니까?");
            System.out.println("1. 예");
            System.out.println("0. 뒤로가기");

            int confirmChoice;
            while (true) {
                System.out.print("선택: ");
                if (scanner.hasNextInt()) {
                    confirmChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (confirmChoice == 0 || confirmChoice == 1) {
                        break;
                    }
                } else {
                    scanner.next();
                }
                System.out.println("❌ 올바른 숫자를 입력하세요.");
            }

            if (confirmChoice == 1) {
                boolean result = taskService.softDeleteTask(loggedInUser, selectedTask.getTaskId());

                if (result) {
                    System.out.println("❗️ '" + selectedTask.getContents() + "' 을(를) 삭제했습니다.");
                } else {
                    System.out.println("❌ 삭제 실패.");
                }
            } else {
                System.out.println("삭제가 취소되었습니다.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}