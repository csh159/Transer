package com.scott.transer.processor;

import com.scott.annotionprocessor.ITask;
import com.scott.transer.task.ITaskHolder;
import com.scott.transer.task.ITaskHolderProxy;
import com.scott.transer.task.ITaskInternalHandler;
import com.scott.transer.task.TaskHolderProxy;
import com.scott.transer.task.TaskState;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017-12-13 15:38</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class TaskProcessor implements ITaskProcessor {

    private List<ITaskHolder> mTasks;
    private ITaskManager mTaskManager;

    @Override
    public void setTaskManager(ITaskManager manager) {
        mTaskManager = manager;
    }

    @Override
    public void setTaskHolders(List<ITaskHolder> taskHolders) {
        mTasks = taskHolders;
    }

    @Override
    public void addTask(ITask task) {
        ITaskHolder holder = new TaskHolderProxy();
        holder.setTask(task);
        mTasks.add(holder);
    }

    @Override
    public void addTasks(List<ITask> tasks) {
        for(ITask task : tasks) {
            ITaskHolder holder = new TaskHolderProxy();
            holder.setTask(task);
            mTasks.add(holder);
        }
    }

    @Override
    public void deleteTask(String taskId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getTaskId() == taskId) {
                mTasks.remove(holder);
                break;
            }
        }
    }

    @Override
    public void deleteGroup(String groupId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getGroupId() == groupId) {
                mTasks.remove(holder);
            }
        }
    }

    @Override
    public void deleteTasks(String[] taskIds) {
        for(ITaskHolder holer : mTasks) {
            for(String taskId : taskIds) {
                if(holer.getTask().getTaskId() == taskId) {
                    mTasks.remove(holer);
                }
            }
        }
    }

    @Override
    public void deleteCompleted() {
        for(ITaskHolder holder : mTasks) {
            if(holder.getState() == TaskState.STATE_FINISH) {
                mTasks.remove(holder);
            }
        }
    }

    @Override
    public void delete(int state) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getState() == state) {
                mTasks.remove(holder);
            }
        }
    }

    @Override
    public void deleteAll() {
        mTasks.clear();
    }

    @Override
    public ITask getTask(String taskId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getTaskId() == taskId) {
                return holder.getTask();
            }
        }
        return null;
    }

    @Override
    public List<ITask> getTasks(String[] taskIds) {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder task : mTasks) {
            for(String taskId : taskIds) {
                if(task.getTask().getTaskId() == taskId) {
                    tasks.add(task.getTask());
                }
            }
        }
        return tasks;
    }

    @Override
    public List<ITask> getGroup(String groupId) {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getGroupId() == groupId) {
                tasks.add(holder.getTask());
            }
        }
        return tasks;
    }

    @Override
    public List<ITask> getAllTasks() {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder holder : mTasks) {
            tasks.add(holder.getTask());
        }
        return tasks;
    }

    @Override
    public List<ITask> getTasks(int state) {
        List<ITask> tasks = new ArrayList<>();
        for(ITaskHolder holder : mTasks) {
            if(holder.getState() == state) {
                tasks.add(holder.getTask());
            }
        }
        return tasks;
    }

    @Override
    public void changeTaskState(int state, String taskId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getTaskId() == taskId) {
                holder.setState(state);
                break;
            }
        }
    }

    @Override
    public void changeTaskStateWithOutSave(int state, String taskId) {
        changeTaskState(state,taskId);
    }

    @Override
    public void changeTasksState(int state, String[] taskId) {
        for(ITaskHolder holder : mTasks) {
            for(String id : taskId) {
                if(holder.getTask().getTaskId() == id) {
                    holder.setState(state);
                }
            }
        }
    }

    @Override
    public void changeTasksState(int state, String groupId) {
        for(ITaskHolder holder : mTasks) {
            if(holder.getTask().getGroupId() == groupId) {
                if(((ITaskHolderProxy)holder).getTaskHandler() == null) {
                    ITaskInternalHandler handler = mTaskManager.getTaskHandler(holder.getType());
                    handler.setTask(holder.getTask());
                    ((ITaskHolderProxy)holder).setTaskHandler(handler);
                }
                holder.setState(state);
            }
        }
    }

    @Override
    public void changeAllTasksState(int state) {
        for(ITaskHolder holder : mTasks) {
            holder.setState(state);
        }
    }

    @Override
    public void updateTask(ITask task) {
        ITask task1 = getTask(task.getTaskId());
        int i = mTasks.indexOf(task1);
        mTasks.remove(task1);
        addTask(task);
    }
}
