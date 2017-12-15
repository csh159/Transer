package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.task.ITaskHolder;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:00</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskProcessor {

    void setTaskHolders(List<ITaskHolder> taskHolders);

    void addTask(ITask task);

    void addTasks(List<ITask> tasks);

    void deleteTask(String taskId);

    void deleteGroup(String groupId);

    void deleteTasks(String[] taskIds);

    void deleteCompleted();

    void delete(int state);

    void deleteAll();

    ITask getTask(String taskId);

    List<ITask> getTasks(String[] taskIds);

    List<ITask> getGroup(String groupId);

    List<ITask> getAllTasks();

    List<ITask> getTasks(int state);

    void changeTaskState(int state,String taskId);

    void changeTasksState(int state,String[] taskId);

    void changeTasksState(int state,String groupId);

    void changeAllTasksState(int state);
}
