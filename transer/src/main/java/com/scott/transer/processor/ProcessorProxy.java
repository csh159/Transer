package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.task.ITaskHolder;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:18</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class ProcessorProxy implements ITaskProcessor {

    ITaskProcessor mProcessor;
    ITaskProcessor mDbProcessor;

    public ProcessorProxy(ITaskProcessor taskProcessor,ITaskProcessor dbProcessor) {
        mProcessor = taskProcessor;
        mDbProcessor = dbProcessor;
    }

    @Override
    public void setTaskHolders(List<ITaskHolder> taskHolders) {
        mProcessor.setTaskHolders(taskHolders);
        mDbProcessor.setTaskHolders(taskHolders);
    }

    @Override
    public void addTask(ITask task) {
        mProcessor.addTask(task);
        mDbProcessor.addTask(task);
    }

    @Override
    public void addTasks(List<ITask> tasks) {
        mProcessor.addTasks(tasks);
        mDbProcessor.addTasks(tasks);
    }

    @Override
    public void deleteTask(String taskId) {
        mProcessor.deleteTask(taskId);
        mDbProcessor.deleteTask(taskId);
    }

    @Override
    public void deleteGroup(String groupId) {
        mProcessor.deleteTask(groupId);
        mDbProcessor.deleteTask(groupId);
    }

    @Override
    public void deleteTasks(String[] taskIds) {
        mProcessor.deleteTasks(taskIds);
        mDbProcessor.deleteTasks(taskIds);
    }

    @Override
    public void deleteCompleted() {
        mProcessor.deleteCompleted();
        mDbProcessor.deleteCompleted();
    }

    @Override
    public void delete(int state) {
        mProcessor.delete(state);
        mDbProcessor.delete(state);
    }

    @Override
    public void deleteAll() {
        mProcessor.deleteAll();
        mDbProcessor.deleteAll();
    }

    @Override
    public ITask getTask(String taskId) {
        ITask task = mProcessor.getTask(taskId);
        if(task == null) {
            task = mDbProcessor.getTask(taskId);
            mProcessor.addTask(mDbProcessor.getTask(taskId));
        }
        return task;
    }

    @Override
    public List<ITask> getTasks(String[] taskIds) {
        List<ITask> tasks = mProcessor.getTasks(taskIds);
        if(tasks == null) {
            tasks = mDbProcessor.getTasks(taskIds);
            mProcessor.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public List<ITask> getGroup(String groupId) {
        List<ITask> tasks = mProcessor.getGroup(groupId);
        if(tasks == null) {
            tasks = mDbProcessor.getGroup(groupId);
            mProcessor.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public List<ITask> getAllTasks() {
        List<ITask> tasks = mProcessor.getAllTasks();
        if(tasks == null) {
            tasks = mDbProcessor.getAllTasks();
            mProcessor.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public List<ITask> getTasks(int state) {
        List<ITask> tasks = mProcessor.getTasks(state);
        if(tasks == null) {
            tasks = mDbProcessor.getTasks(state);
            mProcessor.addTasks(tasks);
        }
        return tasks;
    }

    @Override
    public void changeTaskState(int state, String taskId) {
        mProcessor.changeTaskState(state,taskId);
        mDbProcessor.changeTaskState(state,taskId);
    }

    @Override
    public void changeTasksState(int state, String[] taskId) {
        mProcessor.changeTasksState(state,taskId);
        mDbProcessor.changeTasksState(state,taskId);
    }

    @Override
    public void changeTasksState(int state, String groupId) {
        mProcessor.changeTasksState(state,groupId);
        mDbProcessor.changeTasksState(state,groupId);
    }

    @Override
    public void changeAllTasksState(int state) {
        mProcessor.changeAllTasksState(state);
        mDbProcessor.changeAllTasksState(state);
    }
}
