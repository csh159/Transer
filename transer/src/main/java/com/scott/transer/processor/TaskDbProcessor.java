package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.dao.DaoHelper;
import com.scott.transer.dao.TaskDao;
import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.Task;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:39</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskDbProcessor implements ITaskProcessor {

    private TaskDao mTaskDao;
    public TaskDbProcessor() {
        mTaskDao = DaoHelper.getDbSession().getTaskDao();
    }

    @Override
    public void setTaskManager(ITaskManager manager) {

    }

    @Override
    public void setTaskHolders(List<ITaskHolder> taskHolders) {

    }

    @Override
    public void addTask(ITask task) {
        mTaskDao.insert((Task) task);
    }

    @Override
    public void addTasks(List tasks) {
        mTaskDao.insertInTx(tasks);
    }

    @Override
    public void deleteTask(String taskId) {
        Task task = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.TaskId.eq(taskId))
                .unique();
        mTaskDao.delete(task);
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
