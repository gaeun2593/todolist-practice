package com.todolist.domain.task.service;

import com.todolist.domain.category.dao.CategoryDao;
import com.todolist.domain.task.dao.TaskDao;
import com.todolist.domain.task.model.Task;
import com.todolist.domain.task.model.dto.TaskWithDetailsDto;
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


    // 전체 To Do List 조회
    public List<TaskWithDetailsDto> getAllTasks() throws SQLException {
        List<TaskWithDetailsDto> tasks = taskDao.getAllTasks();

        if(tasks == null) {
            log.error("조회한 투두리스트의 정보가 없거나 DB와 연결하는 과정에서 오류가 발생했습니다.");
            return null;
        }

        return taskDao.getAllTasks();
    }

    // 유저별 To Do List 조회
    public List<TaskWithDetailsDto> getTasksByUser(User loggedInUser) throws SQLException {
        return taskDao.getTasksByUser(loggedInUser.getUserId());
    }

    // 카테고리별 To Do List 조회


    // 키워드 검색 : 키워드 받아서 조회


    // To Do List 내용 등록
    public boolean addTask(Task task) throws SQLException {
        return  taskDao.addTaskContents(task);
    }


    // To Do List 상태 변경 : IN_PROGRESS 에서 COMPLETED 로 변경. 수정일자 등록
    public boolean completeTask(User loggedInUser, int taskId) throws SQLException {
        Task taskToUpdate = new Task();
        taskToUpdate.setTaskId(taskId);
        taskToUpdate.setUserId(loggedInUser.getUserId());

        return taskDao.updateTaskStatus(taskToUpdate);
    }

    // status가 IN_PROGRESS인 To Do List만 조회
    public List<TaskWithDetailsDto> getTasksByUserInProgress(User loggedInUser) throws SQLException {
        return taskDao.getTasksByUserInProgress(loggedInUser.getUserId());
    }

    // TOP3 랭킹 보기


    // To Do List 삭제 : 삭제 정보(is_delete = 1) 업데이트
    public boolean softDeleteTask(User loggedInUser, int taskId) throws SQLException {
        return taskDao.softDeleteTask(taskId, loggedInUser.getUserId());
    }
}
