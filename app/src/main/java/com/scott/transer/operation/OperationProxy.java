package com.scott.transer.operation;

import com.scott.transer.task.ITask;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:18</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class OperationProxy implements ITaskOperation{

    ITaskOperation mMemoryOpeation;
    ITaskOperation mDatabaseOperation;

    @Override
    public void addTask(ITask task) {
        mMemoryOpeation.addTask(task);
        mDatabaseOperation.addTask(task);
    }

    @Override
    public void addTasks(List<ITask> tasks) {
        mMemoryOpeation.addTasks(tasks);
        mDatabaseOperation.addTasks(tasks);
    }

    @Override
    public void deleteTask(String taskId) {
        mMemoryOpeation.deleteTask(taskId);
        mDatabaseOperation.deleteTask(taskId);
    }

    @Override
    public void deleteGroup(String groupId) {
        mMemoryOpeation.deleteTask(groupId);
        mDatabaseOperation.deleteTask(groupId);
    }

    @Override
    public void deleteTasks(String[] taskIds) {
        mMemoryOpeation.deleteTasks(taskIds);
        mDatabaseOperation.deleteTasks(taskIds);
    }

    @Override
    public void deleteCompleted() {
        mMemoryOpeation.deleteCompleted();
        mDatabaseOperation.deleteCompleted();
    }

    @Override
    public void delete(int state) {
        mMemoryOpeation.delete(state);
        mDatabaseOperation.delete(state);
    }

    @Override
    public void deleteAll() {
        mMemoryOpeation.deleteAll();
        mDatabaseOperation.deleteAll();
    }

    @Override
    public ITask getTask(String taskId) {
        ITask task = mMemoryOpeation.getTask(taskId);
        if(task == null) {
            task = mDatabaseOperation.getTask(taskId);
            mMemoryOpeation.addTask(mDatabaseOperation.getTask(taskId));
        }
        return task;
    }

    @Override
    public List<ITask> getTasks(String[] taskIds) {
        List<ITask> tasks = mMemoryOpeation.getTasks(taskIds);
        if(tasks == null) {
            tasks = mDatabaseOperation.getTasks(taskIds);
            mMemoryOpeation.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public List<ITask> getGroup(String groupId) {
        List<ITask> tasks = mMemoryOpeation.getGroup(groupId);
        if(tasks == null) {
            tasks = mDatabaseOperation.getGroup(groupId);
            mMemoryOpeation.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public List<ITask> getAllTasks() {
        List<ITask> tasks = mMemoryOpeation.getAllTasks();
        if(tasks == null) {
            tasks = mDatabaseOperation.getAllTasks();
            mMemoryOpeation.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public List<ITask> getTasks(int state) {
        List<ITask> tasks = mMemoryOpeation.getTasks(state);
        if(tasks == null) {
            tasks = mDatabaseOperation.getTasks(state);
            mMemoryOpeation.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public void changeTaskState(int state, String taskId) {
        mMemoryOpeation.changeTaskState(state,taskId);
        mDatabaseOperation.changeTaskState(state,taskId);
    }

    @Override
    public void changeTasksState(int state, String[] taskId) {
        mMemoryOpeation.changeTasksState(state,taskId);
        mDatabaseOperation.changeTasksState(state,taskId);
    }

    @Override
    public void changeTasksState(int state, String groupId) {
        mMemoryOpeation.changeTasksState(state,groupId);
        mDatabaseOperation.changeTasksState(state,groupId);
    }

    @Override
    public void changeAllTasksState(int state) {
        mMemoryOpeation.changeAllTasksState(state);
        mDatabaseOperation.changeAllTasksState(state);
    }
}
