package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.annotionprocessor.TaskType;
import com.scott.transer.dao.DaoHelper;
import com.scott.transer.dao.TaskDao;
import com.scott.transer.task.Task;
import com.scott.transer.task.TaskState;
import com.scott.transer.task.TaskTypeConverter;


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
//        List<Task> tasks = mTaskDao
//                .queryBuilder()
//                .where(TaskDao.Properties.TaskId.in(taskIds))
//                .list();
//        mTaskDao.deleteInTx(tasks);
    }

    @Override
    public void deleteCompleted(TaskType type) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.State.eq(TaskState.STATE_FINISH))
                .where(TaskDao.Properties.Type.eq(type))
                .list();
        mTaskDao.deleteInTx(tasks);
    }

    @Override
    public void delete(int state,TaskType type) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.State.eq(state))
                .where(TaskDao.Properties.Type.eq(type))
                .list();
        mTaskDao.deleteInTx(tasks);
    }

    @Override
    public void deleteAll(TaskType type) {
        TaskTypeConverter converter = new TaskTypeConverter();
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.Type.eq(converter.convertToDatabaseValue(type)))
                .list();
        mTaskDao.deleteInTx(tasks);
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
//        List<Task> tasks = mTaskDao
//                .queryBuilder()
//                .where(TaskDao.Properties.TaskId.in(taskIds))
//                .list();
//        List<ITask> tasks1 = new ArrayList<>();
//        tasks1.addAll(tasks);
        return null;
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
    public List<ITask> getAllTasks(TaskType type) {

        TaskTypeConverter converter = new TaskTypeConverter();
        int nType = converter.convertToDatabaseValue(type);

        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.Type.eq(nType))
                .list();
        List<ITask> tasks1 = new ArrayList<>();
        tasks1.addAll(tasks);
        return tasks1;
    }

    @Override
    public List<ITask> getTasks(int state,TaskType type) {
        List<Task> tasks = mTaskDao
                .queryBuilder()
                .where(TaskDao.Properties.State.eq(state))
                .where(TaskDao.Properties.Type.eq(type))
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
//        List<Task> tasks = mTaskDao
//                .queryBuilder()
//                .where(TaskDao.Properties.TaskId.in(taskId))
//                .list();
//        for(Task task : tasks) {
//            task.setState(state);
//        }

        //mTaskDao.updateInTx(tasks);
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
    public void changeAllTasksState(int state,TaskType type) {
        List<Task> tasks = mTaskDao.loadAll();
        for(Task task : tasks) {
            if(type == task.getType()) {
                task.setState(state);
            }
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
