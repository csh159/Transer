package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.dao.DaoHelper;
import com.scott.transer.dao.TaskDao;
import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.Task;
import com.scott.transer.task.TaskState;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
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
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.GroupId.eq(groupId))
                .list();
        mTaskDao.deleteInTx(tasks);
    }

    @Override
    public void deleteTasks(String[] taskIds) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.TaskId.in(taskIds))
                .list();
        mTaskDao.deleteInTx(tasks);
    }

    @Override
    public void deleteCompleted() {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.State.eq(TaskState.STATE_FINISH))
                .list();
        mTaskDao.deleteInTx(tasks);
    }

    @Override
    public void delete(int state) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.State.eq(state))
                .list();
        mTaskDao.deleteInTx(tasks);
    }

    @Override
    public void deleteAll() {
        mTaskDao.deleteAll();
    }

    @Override
    public ITask getTask(String taskId) {
        ITask task = mTaskDao.queryBuilder()
                .where(TaskDao.Properties.TaskId.eq(taskId))
                .unique();
        return task;
    }

    @Override
    public List<ITask> getTasks(String[] taskIds) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.TaskId.in(taskIds))
                .list();
        List<ITask> tasks1 = new ArrayList<>();
        tasks1.addAll(tasks);
        return tasks1;
    }

    @Override
    public List<ITask> getGroup(String groupId) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.GroupId.eq(groupId))
                .list();
        List<ITask> tasks1 = new ArrayList<>();
        tasks1.addAll(tasks);
        return tasks1;
    }

    @Override
    public List<ITask> getAllTasks() {
        List<Task> tasks = mTaskDao.loadAll();
        List<ITask> tasks1 = new ArrayList<>();
        tasks1.addAll(tasks);
        return tasks1;
    }

    @Override
    public List<ITask> getTasks(int state) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.State.eq(state))
                .list();
        List<ITask> tasks1 = new ArrayList<>();
        tasks1.addAll(tasks);
        return tasks1;
    }

    @Override
    public void changeTaskState(int state, String taskId) {
        Task task = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.TaskId.eq(taskId))
                .unique();
        task.setState(state);

        mTaskDao.update(task);
    }

    @Override
    public void changeTaskStateWithOutSave(int state, String taskId) {

    }

    @Override
    public void changeTasksState(int state, String[] taskId) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.TaskId.in(taskId))
                .list();
        for(Task task : tasks) {
            task.setState(state);
        }

        mTaskDao.updateInTx(tasks);
    }

    @Override
    public void changeTasksState(int state, String groupId) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.GroupId.eq(groupId))
                .list();
        for(Task task : tasks) {
            task.setState(state);
        }

        mTaskDao.updateInTx(tasks);
    }

    @Override
    public void changeAllTasksState(int state) {
        List<Task> tasks = mTaskDao.loadAll();
        for(Task task : tasks) {
            task.setState(state);
        }

        mTaskDao.updateInTx(tasks);
    }

    @Override
    public void updateTask(ITask task) {
        mTaskDao.update((Task) task);
    }

    @Override
    public void updateTaskWithoutSave(ITask task) {

    }
}
