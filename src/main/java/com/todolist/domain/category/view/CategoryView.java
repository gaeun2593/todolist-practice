package com.todolist.domain.category.view;

import com.todolist.domain.category.model.Category;
import com.todolist.domain.category.service.CategoryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CategoryView {
    private CategoryService categoryService;
    private Scanner scanner;

    public CategoryView(Connection connection) {
        this.categoryService = new CategoryService(connection);
        this.scanner = new Scanner(System.in);
    }

    /**
     * 📌 카테고리 등록 (CREATE)
     * - 카테고리 title 을 입력받아 새로운 카테고리를 등록
     */
    public Category addCategory() {
        String title;

        // 카테고리 제목 길이 제한 (10자 이하)
        while (true) {
            System.out.print("✏️ 추가하실 카테고리 제목을 입력해주세요 (최대 10자): ");
            title = scanner.nextLine();

            if (title.length() <= 10) {
                break;
            } else {
                System.out.println("❌ 제목은 최대 10자까지 입력할 수 있습니다. 다시 입력해주세요.");
            }
        }

        Category category = new Category(0, title, null);

        try {
            boolean success = categoryService.addCategory(category);
            if (success) {
                System.out.println("✅ 카테고리 등록이 완료되었습니다.");
                return categoryService.getCategoryByTitle(title);
            } else {
                System.out.println("❌ 카테고리 등록에 실패하였습니다.");
            }
        } catch (SQLException e) {
            System.out.println("❌ 카테고리 등록 중 오류가 발생했습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
