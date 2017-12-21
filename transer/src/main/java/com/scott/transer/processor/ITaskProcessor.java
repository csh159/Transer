package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.Task;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:00</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public interface ITaskProcessor {

    void setTaskManager(ITaskManager manager);

    void addTask(ITask task);

    void addTasks(List<ITask> tasks);

    void deleteTask(String taskId);

    void deleteGroup(String groupId);

    void deleteTasks(String[] taskIds);

    void deleteCompleted(TaskType type);

    void delete(int state,TaskType type);

    void deleteAll(TaskType type);

    ITask getTask(String taskId);

    List<ITask> getTasks(String[] taskIds);

    List<ITask> getGroup(String groupId);

    List<ITask> getAllTasks(TaskType type);

    List<ITask> getTasks(int state, TaskType type);

    void changeTaskState(int state,String taskId);

    void changeTaskStateWithOutSave(int state,String taskId);

    void changeTasksState(int state,String[] taskId);

    void changeTasksState(int state,String groupId);

    void changeAllTasksState(int state,TaskType type);

    void updateTask(ITask task);

    void updateTaskWithoutSave(ITask task);
}
