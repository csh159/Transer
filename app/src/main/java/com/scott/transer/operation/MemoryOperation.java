package com.scott.transer.operation;

import com.scott.transer.task.ITask;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:38</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class MemoryOperation implements ITaskOperation {

    @Override
    public void addTask(ITask task) {

    }

    @Override
    public void addTasks(List<ITask> tasks) {

    }

    @Override
    public void deleteTask(String taskId) {

    }

    @Override
    public void deleteGroup(String groupId) {

    }

    @Override
    public void deleteTasks(String[] taskIds) {

    }

    @Override
    public void deleteCompleted() {

    }

    @Override
    public void delete(int state) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public ITask getTask(String taskId) {
        return null;
    }

    @Override
    public List<ITask> getTasks(String[] taskIds) {
        return null;
    }

    @Override
    public List<ITask> getGroup(String groupId) {
        return null;
    }

    @Override
    public List<ITask> getAllTasks() {
        return null;
    }

    @Override
    public List<ITask> getTasks(int state) {
        return null;
    }

    @Override
    public void changeTaskState(int state, String taskId) {

    }

    @Override
    public void changeTasksState(int state, String[] taskId) {

    }

    @Override
    public void changeTasksState(int state, String groupId) {

    }

    @Override
    public void changeAllTasksState(int state) {

    }
}
