package com.todolist.domain.task.service;

import com.todolist.domain.category.dao.CategoryDao;
import com.todolist.domain.task.dao.TaskDao;
import com.todolist.domain.task.model.Task;
import com.todolist.domain.task.model.dto.TaskWithDetailsDto;
import com.todolist.domain.task.model.dto.TodoAndCategoryDto;
import com.todolist.domain.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskDao taskDao;
    private final CategoryDao categoryDao;
    private final Connection connection;

    public TaskService(Connection connection) {
        this.connection = connection;
        this.taskDao = new TaskDao(connection);
        this.categoryDao = new CategoryDao(connection);
    }

    /**
     * 📌 모든 To Do List 조회 (READ)
     * - 데이터 검증 후 반환
     */
    public List<TaskWithDetailsDto> getAllTasks() throws SQLException {
        List<TaskWithDetailsDto> tasks = taskDao.getAllTasks();

        if(tasks == null) {
            log.error("조회한 투두리스트의 정보가 없거나 DB와 연결하는 과정에서 오류가 발생했습니다.");
            return null;
        }

        return taskDao.getAllTasks();
    }

    /**
     * 📌 유저별 To Do List 조회 (READ)
     * - 이 메서드는 주어진 카테고리 title 을 기반으로 데이터베이스에서 카테고리를 조회합니다.
     * @param loggedInUser 조회할 로그인 상태의 유저
     * @return 조회된 'TaskWithDetailsDto' 리스트를 반환
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */
    public List<TaskWithDetailsDto> getTasksByUser(User loggedInUser) throws SQLException {
        return taskDao.getTasksByUser(loggedInUser.getUserId());
    }

    /**
     * 📌 카테고리별 To Do List 조회 (READ)
     * - 이 메서드는 주어진 카테고리 ID를 기반으로 해당 카테고리에 속한 To Do List 항목들을 데이터베이스에서 조회합니다.
     * @param categoryId 조회할 카테고리의 아이디
     * @return 조회된 'TodoAndCategoryDto' 리스트를 반환
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */
    public List<TodoAndCategoryDto> getTasksByCategory(int categoryId) throws SQLException {
        return taskDao.getTasksByCategory(categoryId);
    }

    /**
     * 📌 To Do List 내용 등록 (CREATE)
     * - 이 메서드는 로그인한 사용자에 대한 작업(Task)을 추가합니다.
     * @param loggedInUser 로그인한 사용자 객체
     * @param task 추가할 작업(Task) 객체
     * @return taskDao 의 addTaskContents() 메서드 반환값 (작업 추가 성공 여부)
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     * @throws IllegalArgumentException 입력된 작업(Task)의 내용이 유효하지 않거나 다른 오류가 발생할 경우 발생
     */
    public boolean addTask(User loggedInUser, Task task) throws SQLException {
        task.setUserId(loggedInUser.getUserId());
        return  taskDao.addTaskContents(task);
    }

    /**
     * 📌 To Do List 작업 완료 처리 (UPDATE)
     * - 이 메서드는 주어진 `taskId`와 로그인한 사용자(`loggedInUser`)를 기반으로 작업(Task)의 상태를 완료로 업데이트합니다.
     * @param loggedInUser 작업을 완료할 사용자의 정보
     * @param taskId 완료 처리할 작업의 아이디
     * @return 작업 상태 업데이트 성공 여부 (`true`/`false`)
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */
    public boolean completeTask(User loggedInUser, int taskId) throws SQLException {
        Task taskToUpdate = new Task();
        taskToUpdate.setTaskId(taskId);
        taskToUpdate.setUserId(loggedInUser.getUserId());

        return taskDao.updateTaskStatus(taskToUpdate);
    }

    /**
     * 📌 사용자의 진행 중인 To Do List 조회 (READ)
     * - 이 메서드는 로그인한 사용자의 아이디를 기반으로 해당 사용자가 진행 중인 작업(Task)을 데이터베이스에서 조회합니다.
     * @param loggedInUser 진행 중인 작업을 조회할 사용자의 정보
     * @return 해당 사용자가 진행 중인 작업들의 `TaskWithDetailsDto` 리스트
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */    public List<TaskWithDetailsDto> getTasksByUserInProgress(User loggedInUser) throws SQLException {
        return taskDao.getTasksByUserInProgress(loggedInUser.getUserId());
    }

    /**
     * 📌 To Do List 작업 소프트 삭제 (UPDATE)
     * - 이 메서드는 주어진 `taskId`와 로그인한 사용자의 아이디를 기반으로 작업(Task)을 소프트 삭제합니다.
     * @param loggedInUser 작업을 소프트 삭제할 사용자의 정보
     * @param taskId 소프트 삭제할 작업의 아이디
     * @return 작업 소프트 삭제 성공 여부 (`true`/`false`)
     * @throws SQLException 데이터베이스 접근 중 오류가 발생할 경우 발생
     */    public boolean softDeleteTask(User loggedInUser, int taskId) throws SQLException {
        return taskDao.softDeleteTask(taskId, loggedInUser.getUserId());
    }
}
